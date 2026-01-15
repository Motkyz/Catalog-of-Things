package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalogofthings.R
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.databinding.FragmentStartAppBinding
import com.example.catalogofthings.databinding.IncludeNotesNotFoundBinding
import com.example.catalogofthings.presenter.actionDialog.NoteActionDialog
import com.example.catalogofthings.presenter.adapters.ListNotesAdapter
import dev.androidbroadcast.vbpd.viewBinding

class StartFragment: BaseFolderFragment(R.layout.fragment_start_app) {
    private val binding: FragmentStartAppBinding by viewBinding(FragmentStartAppBinding::bind)

    override fun setTagSpinner() {
        tagSpinner = binding.searchBarStartFragment.tagSpinnerFilterInFolder
    }

    override fun setVariantSpinner() {
        variantSpinner = binding.searchBarStartFragment.buttonSortInFolder
    }

    override fun setFragmentBindings() {
        bindingNotesNotFound = binding.includedNotesNotFoundStartFragment
        bindingRecyclerNotes = binding.includedRecyclerNotesStartFragment
        bindingSearchBar = binding.searchBarStartFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(bindingRecyclerNotes.recyclerNotes) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@StartFragment.adapter
        }

        setBindings()
        setObserves()
    }

    override fun setObserves() {
        super.setObserves()

        viewModel.getNotes(0)
    }

    override fun setBindings(){
        super.setBindings()

        binding.addNew.setOnClickListener {
            findNavController().navigate(
                R.id.action_startFragment_to_noteFragment,
                bundleOf(
                    "id" to 0,
                    "parentId" to 0
                )
            )
        }

        binding.addNew.setOnLongClickListener {
            val bottomSheet = CreateFolderBottomSheet.newInstance(parentId = 0)
            bottomSheet.setOnFolderCreatedListener { newFolder ->
                viewModel.createFolder(newFolder)
            }
            bottomSheet.show(childFragmentManager, "create_folder_bottom_modal")
            true
        }
    }

    override fun onNoteClick(note: NoteEntity) {
        if (note.isFolder) {
            findNavController().navigate(
                R.id.action_startFragment_to_folderFragment,
                bundleOf(
                    "id" to note.noteId
                )
            )
        } else {
            findNavController().navigate(
                R.id.action_startFragment_to_noteFragment,
                bundleOf(
                    "id" to note.noteId
                )
            )
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
                    bottomSheet.dismiss()
                }
                bottomSheet.show(childFragmentManager, null)
            }
        )
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}