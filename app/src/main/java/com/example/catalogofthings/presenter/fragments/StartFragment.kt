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

        initAllObserve()
        initTagSpinner()

        adapter = ListNotesAdapter(
            onNoteClick = ::onNoteClick,
            onNoteLongClick = ::onNoteLongClick
        )

        with(binding.includedRecyclerNotesStartFragment.recyclerNotes) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@StartFragment.adapter
        }

        setBindings()
    }

    private fun initTagSpinner(){
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
    }

    private fun onTagFilterClick(tag: TagEntity?) {
        viewModel.setTagFilter(tag?.tagId)
    }

    private fun setBindings(){
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

        binding.searchBarStartFragment.searchInFolderFragment.doAfterTextChanged {
            viewModel.setSearchFilter(it.toString())
        }
    }

    private fun initAllObserve(){
        viewModel.getTags()

        viewModel.getNotes(0)

        viewModel.notes.observe(viewLifecycleOwner) { notesWithTags ->
            val notes = notesWithTags ?: emptyList()
            val noteEntities = notes.map { it.note }

            if (notes.isEmpty()) {
                binding.includedNotesNotFoundStartFragment.root.visibility = View.VISIBLE
                binding.includedRecyclerNotesStartFragment.recyclerNotes.visibility = View.GONE
            } else {
                binding.includedNotesNotFoundStartFragment.root.visibility = View.GONE
                binding.includedRecyclerNotesStartFragment.recyclerNotes.visibility = View.VISIBLE

                adapter.submitList(noteEntities)
            }
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

    private fun onNoteLongClick(note: NoteEntity) {
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
        val component = context.appComponent
        component.inject(this)
        super.onAttach(context)
    }
}