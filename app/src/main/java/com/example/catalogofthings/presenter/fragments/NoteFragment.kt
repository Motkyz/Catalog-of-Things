package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalogofthings.R
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.data.ImageConverter
import com.example.catalogofthings.data.model.ImageEntity
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteFull
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.databinding.FragmentNoteBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import com.example.catalogofthings.presenter.actionDialog.ImageActionsDialog
import com.example.catalogofthings.presenter.adapters.ListImagesAdapter
import com.example.catalogofthings.presenter.adapters.ListTagsInNoteAdapter
import com.example.catalogofthings.presenter.viewModels.NoteFragmentViewModel
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class NoteFragment : Fragment(R.layout.fragment_note) {

    private val binding: FragmentNoteBinding by viewBinding(FragmentNoteBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: NoteFragmentViewModel by viewModels { viewModelFactory }

    private lateinit var tagsAdapter: ListTagsInNoteAdapter
    private lateinit var imagesAdapter: ListImagesAdapter
    private var isNewNote : Boolean = false
    private var oldNote : NoteEntity? = null
    private var parentId: Int = 0

//     Выбор изображения из галереи
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { uri ->
            lifecycleScope.launch {
                val bytes = ImageConverter.toByteArray(requireContext(), uri)
                    ?: return@launch

                val newImage = ImageEntity(imageData = bytes)
                viewModel.loadImage(newImage)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteId = arguments?.getInt("id")
        isNewNote = noteId == 0

        parentId = arguments?.getInt("parentId") ?: 0
        setFragmentResult(
            "requestKey",
            bundleOf(
                "folderId" to parentId
            )
        )

        initAdapters()
        setupUI()
        setupListeners()

        if (isNewNote) {
            binding.dateCreateNote.text = "Только что"
            binding.includeHeaderNote.titleFolder.requestFocus()
        } else if (noteId != null){
            viewModel.getNote(noteId)
        }

        viewModel.note.observe(viewLifecycleOwner) { noteFull ->
            if (noteFull != null) {
                oldNote = noteFull.note
                updateUI(noteFull)
            }
        }

        viewModel.noteTags.observe(viewLifecycleOwner) {
            tagsAdapter.submitList(it)
        }

        viewModel.noteImages.observe(viewLifecycleOwner) {
            imagesAdapter.submitList(it)
        }
    }

    private fun updateUI(noteInfo : NoteFull){

        binding.includeHeaderNote.titleFolder.setText(noteInfo.note.title)

        binding.edittextForDescriptionNote.setText(noteInfo.note.description)

        binding.dateCreateNote.text = android.text.format.DateFormat.format(
            "dd.MM.yyyy",
            Date(noteInfo.note.date)
        )
    }

    private fun initAdapters() {

        tagsAdapter = ListTagsInNoteAdapter()

        imagesAdapter = ListImagesAdapter(
            onImageClick = {
                val bytes = it.imageData
                FullscreenImageDialog.newInstance(bytes!!)
                    .show(parentFragmentManager, "fullscreen_image")
            },
            onImageLongClick = {
                Log.d("onImageLongClick", "НАЖАЛОСЬ")
                ImageActionsDialog.show(
                    context = requireContext(),
                    image = it,
                    onDelete = {
                        (::deleteImage)(it)
                    }
                )
            }
        )

        binding.recyclerTagsNotes.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = tagsAdapter
        }

        binding.recyclerImages.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = imagesAdapter
        }
    }

    private fun setupUI() {
        binding.includeHeaderNote.icBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveNote.setOnClickListener {
            val title = binding.includeHeaderNote.titleFolder.text?.toString()?.trim() ?: ""
            val description = binding.edittextForDescriptionNote.text?.toString()?.trim() ?: ""

            if (title.isBlank() && description.isBlank() &&
                viewModel.noteImages.value?.isEmpty() == true &&
                viewModel.noteTags.value?.isEmpty() == true) {
                findNavController().popBackStack()
                return@setOnClickListener
            }

            val image =
                if (viewModel.noteImages.value.isNullOrEmpty()) null
                else viewModel.noteImages.value?.get(0)?.imageData

            val updatedNote = NoteEntity(
                title = title,
                description = description,
                isFolder = false,
                icon = image,
                parentId = parentId
            )

            if (isNewNote) {
                viewModel.createNote(
                    noteEntity = updatedNote,
                )
            } else {
                viewModel.updateNote(
                    oldNote = oldNote ?: updatedNote,
                    newNote = updatedNote,
                )
            }
        }

        viewModel.success.observe(viewLifecycleOwner) {
            if (it == true){
                findNavController().popBackStack()
            }
        }
    }

    private fun setupListeners() {
        binding.addPhotoNote.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.changeTags.setOnClickListener {
            val bottomSheet = ChooseTagsBottomSheet()
            bottomSheet.setOnTagClick(::onTagClick)
            bottomSheet.show(childFragmentManager, null)
        }
    }

    private fun onTagClick(tag: TagEntity) {
        val selectedTags = viewModel.noteTags.value ?: emptyList()
        val updatedList = if (tag in selectedTags) {
            selectedTags - tag
        } else {
            selectedTags + tag
        }
        viewModel.updateTags(updatedList)
    }

    private fun deleteImage(image : ImageEntity){
        viewModel.deleteImage(image)
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}