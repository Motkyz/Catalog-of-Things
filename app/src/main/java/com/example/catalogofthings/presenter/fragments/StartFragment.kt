package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalogofthings.R
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.databinding.FragmentStartAppBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import com.example.catalogofthings.presenter.actionDialog.NoteActionDialog
import com.example.catalogofthings.presenter.adapters.ListNotesAdapter
import com.example.catalogofthings.presenter.adapters.TagSpinnerAdapter
import com.example.catalogofthings.presenter.viewModels.FolderViewModel
import com.google.android.material.button.MaterialButton
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

class StartFragment: Fragment(R.layout.fragment_start_app) {
    private lateinit var tagSpinner: Spinner
    private lateinit var tagAdapter: TagSpinnerAdapter
    private val binding: FragmentStartAppBinding by viewBinding(FragmentStartAppBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: FolderViewModel by viewModels {viewModelFactory}
    private lateinit var adapter : ListNotesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNotes(0)

        tagSpinner = binding.searchBarStartFragment.tagSpinnerFilterInFolder

        tagAdapter = TagSpinnerAdapter(requireContext(), showEmptyOption = true)
        tagAdapter.setOnTagFilterClick(::onTagFilterClick)
        tagSpinner.adapter = tagAdapter

        tagSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedTag = tagAdapter.getItem(position)

                if (view is MaterialButton) {
                    if (selectedTag == null) {
                        view.text = "Не выбран"
                    } else {
                        view.text = selectedTag.title
                        view.iconTint = ColorStateList.valueOf(selectedTag.color)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        adapter = ListNotesAdapter(
            onNoteClick = ::onNoteClick,
            onNoteLongClick = ::onNoteLongClick
        )

        with(binding.includedRecyclerNotesStartFragment.recyclerNotes) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@StartFragment.adapter
        }

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
            findNavController().navigate(
                R.id.action_startFragment_to_folderFragment,
                bundleOf(
                    "id" to -1,
                    "parentId" to 0
                )
            )
            true
        }

        binding.searchBarStartFragment.searchInFolderFragment.doAfterTextChanged {
            viewModel.setSearchFilter(it.toString())
        }

        viewModel.notes.observe(viewLifecycleOwner) { notesWithTags ->
            val noteEntities = notesWithTags?.map { it.note } ?: emptyList()
            adapter.submitList(noteEntities)
        }

        viewModel.tags.observe(viewLifecycleOwner) { tags ->
            tagAdapter.setData(tags ?: emptyList())
        }

        viewModel.searchFilter.observe(viewLifecycleOwner) {
            viewModel.getNotesByFilters()
        }

        viewModel.tagFilter.observe(viewLifecycleOwner) {
            viewModel.getNotesByFilters()
            tagSpinner.setSelection(tagAdapter.getPosition(it))
        }
    }

    fun onNoteClick(note: NoteEntity) {
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

    fun onTagFilterClick(tag: TagEntity?) {
        viewModel.setTagFilter(tag?.tagId)
    }

    fun onNoteLongClick(note: NoteEntity) {
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

    override fun onAttach(context: Context) {
        val component = context.appComponent
        component.inject(this)
        super.onAttach(context)
    }
}