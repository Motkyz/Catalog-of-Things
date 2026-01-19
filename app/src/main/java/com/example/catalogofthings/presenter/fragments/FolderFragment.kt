package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import com.example.catalogofthings.R
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.databinding.FragmentOpenFolderBinding
import com.example.catalogofthings.presenter.actionDialog.NoteActionDialog
import com.example.catalogofthings.presenter.adapters.ListNotesAdapter
import dev.androidbroadcast.vbpd.viewBinding

class FolderFragment : BaseFolderFragment(R.layout.fragment_open_folder) {

    private val binding: FragmentOpenFolderBinding by viewBinding(FragmentOpenFolderBinding::bind)

    override fun setTagSpinner() {
        tagSpinner = binding.includeSearchBarFolderFragment.tagSpinnerFilterInFolder
    }

    override fun setVariantSpinner() {
        bindingNotesNotFound = binding.includedNotesNotFoundFolderFragment
        bindingRecyclerNotes = binding.includedRecyclerNotesFolderFragment
        variantSpinner = binding.includeSearchBarFolderFragment.buttonSortInFolder
    }

    override fun setFragmentBindings() {
        bindingSearchBar = binding.includeSearchBarFolderFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val folderId = arguments?.getInt("id") ?: 0

        if (folderId > 0 && viewModel.currentFolder.value == null) {
            viewModel.setFolder(folderId)
        }

        setFragmentResultListener("requestKey") {requestKey, bundle ->
            val folderId = bundle.getInt("folderId")
            if (folderId > 0) viewModel.setFolder(folderId)
        }

        with(bindingRecyclerNotes.recyclerNotes) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FolderFragment.adapter
        }

        setBindings()
        setObserves()
    }

    override fun onNoteClick(note: NoteEntity) {
        val parent = viewModel.currentFolder.value?.note?.noteId
        val bundle = bundleOf(
            "id" to note.noteId,
            "parentId" to parent
        )
        if (findNavController().currentBackStackEntry?.destination?.id == R.id.folderFragment) {
            if (note.isFolder) {
                findNavController().navigate(
                    R.id.action_folderFragment_self,
                    bundle
                )
            } else {
                findNavController().navigate(
                    R.id.action_folderFragment_to_noteFragment,
                    bundle
                )
            }
        }
    }

    override fun onNoteLongClick(note: NoteEntity) {
        NoteActionDialog.show(
            context = requireContext(),
            note = note,
            onDelete = {
                viewModel.deleteNote(note)
            },
            replaceInFolder = {
                val bottomSheet = ChooseFolderBottomSheet()
                bottomSheet.setNote(note)
                bottomSheet.setOnFolderClick{
                    val newParent = it.noteId
                    viewModel.updateFolder(note, note.copy(parentId = newParent))
                }
                bottomSheet.show(childFragmentManager, null)
            }
        )
    }

    private fun updateFolder(title: String) {
        val thisFolder = viewModel.currentFolder.value?.note

        if (thisFolder != null) {
            val newFolder = NoteEntity(
                title = title,
                description = "",
                parentId = thisFolder.parentId
            )

            viewModel.updateFolder(
                thisFolder,
                newFolder
            )
        }
    }

    override fun setObserves() {
        super.setObserves()

        viewModel.currentFolder.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.getNotes(it.note.noteId)
                binding.includeHeaderFolder.titleFolder.setText(it.note.title)
            }
        }
    }

    override fun setBindings() {
        super.setBindings()

        binding.addNewFolder.setOnClickListener {
            val parentId = viewModel.currentFolder.value?.note?.noteId ?: 0
            findNavController().navigate(
                R.id.action_folderFragment_to_noteFragment,
                bundleOf(
                    "id" to 0,
                    "parentId" to parentId
                )
            )
        }

        binding.addNewFolder.setOnLongClickListener {
            val currentParentId = viewModel.currentFolder.value?.note?.noteId ?: 0

            val bottomSheet = CreateFolderBottomSheet.newInstance(parentId = currentParentId)
            bottomSheet.setOnFolderCreatedListener { newFolder ->
                viewModel.createFolder(newFolder)
                bottomSheet.dismiss()
            }
            bottomSheet.show(childFragmentManager, "create_folder_bottom_modal")
            true
        }

        binding.includeHeaderFolder.titleFolder.doAfterTextChanged {
            if (viewModel.currentFolder.value?.note?.title != it.toString())
                updateFolder(it.toString())
        }

        binding.includeSearchBarFolderFragment.searchInFolderFragment.doAfterTextChanged {
            viewModel.setSearchFilter(it.toString())
            viewModel.getNotesByFilters()
        }

        binding.includeHeaderFolder.icBackArrow.setOnClickListener {
            val parent = viewModel.currentFolder.value?.note?.parentId
            if (parent != null && parent != 0) {
                viewModel.setFolder(parent)
            } else {
                viewModel.setFolder(0)
            }
            findNavController().popBackStack()
        }
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}