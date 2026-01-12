package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalogofthings.R
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.data.BitmapConverter
import com.example.catalogofthings.data.model.ImageEntity
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteFull
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.databinding.FragmentNoteBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import com.example.catalogofthings.presenter.MainViewModel
import com.example.catalogofthings.presenter.adapters.ListImagesAdapter
import com.example.catalogofthings.presenter.adapters.ListTagsInNoteAdapter
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Date
import javax.inject.Inject

class NoteFragment : Fragment(R.layout.fragment_note) {

    private val binding: FragmentNoteBinding by viewBinding(FragmentNoteBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private lateinit var tagsAdapter: ListTagsInNoteAdapter
    private lateinit var imagesAdapter: ListImagesAdapter

    private val currentImages = mutableListOf<ImageEntity>()
    private val currentTags = mutableListOf<TagEntity>()
    private var isNewNote : Boolean = false
    private var oldNote : NoteEntity? = null

//     Выбор изображения из галереи
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { uri ->
            lifecycleScope.launch {
                val bytes = uriToByteArray(uri) ?: return@launch
                val newImage = ImageEntity(imageData = bytes)
                viewModel.loadImage(newImage)
                currentImages.add(newImage)
                imagesAdapter.submitList(currentImages.toList())
            }
        }
    }

    private fun uriToByteArray(uri: Uri): ByteArray? {
        return try {
            requireContext().contentResolver.openInputStream(uri)?.use { input ->
                ByteArrayOutputStream().use { output ->
                    input.copyTo(output)
                    output.toByteArray()
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteId = arguments?.getString("id")
        isNewNote = noteId == "0"

        initAdapters()
        setupUI()
        setupListeners()

        if (isNewNote) {
            binding.dateCreateNote.text = "Только что"
            binding.includeHeaderNote.titleFolder.requestFocus()
        } else {
            viewModel.getNote(noteId!!.toInt())
        }
        viewModel.note.observe(viewLifecycleOwner) { noteFull ->
            if (noteFull != null) {
                Log.d("ЗАГРУЖАЕМ НОТУ", noteFull.toString())
                oldNote = noteFull.note
                updateUI(noteFull)
            }
        }

        viewModel.images.observe(viewLifecycleOwner){
            Log.d("ЛАЛА В СТАРТЕ", it.toString())
        }

    }

    private fun updateUI(noteInfo : NoteFull){

        binding.includeHeaderNote.titleFolder.setText(noteInfo.note.title)

        binding.edittextForDescriptionNote.setText(noteInfo.note.description)

        binding.dateCreateNote.text = android.text.format.DateFormat.format(
            "dd.MM.yyyy",
            Date(noteInfo.note.date)
        )

        currentTags.clear()
        currentTags.addAll(noteInfo.tags)
        tagsAdapter.submitList(currentTags.toList())

        currentImages.clear()
        Log.d("CI", currentImages.toString())
        currentImages.addAll(noteInfo.images)
        imagesAdapter.submitList(currentImages.toList())
    }

    private fun initAdapters() {

        tagsAdapter = ListTagsInNoteAdapter(
            onTagClick = {
                findNavController().navigate(R.id.action_noteFragment_to_chooseTagBottomSheet)
            }
        )

        imagesAdapter = ListImagesAdapter(
            onImageClick = { /* TODO нужно сделать просмотр изображения */ }
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

            val title = binding.includeHeaderNote.titleFolder.text?.toString()?.trim() ?: ""
            val description = binding.edittextForDescriptionNote.text?.toString()?.trim() ?: ""

            if (title.isBlank() && description.isBlank() &&
                currentImages.isEmpty() && currentImages.isEmpty()
            ) {
                findNavController().popBackStack()
                return@setOnClickListener
            }

            val updatedNote = NoteEntity(
                title = title,
                description = description,
                isFolder = false,
            )

            if (isNewNote) {
                viewModel.createNote(
                    noteEntity = updatedNote,
                    tags = currentTags
                )
            } else {
                viewModel.updateNote(
                    oldNote = oldNote ?: updatedNote,
                    newNote = updatedNote,
                    tags = currentTags
                )
            }

            findNavController().popBackStack()
        }
    }

    private fun setupListeners() {
        binding.addPhotoNote.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}