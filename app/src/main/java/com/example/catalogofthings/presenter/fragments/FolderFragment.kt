package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import com.example.catalogofthings.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.databinding.FragmentOpenFolderBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import com.example.catalogofthings.presenter.adapters.ListNotesAdapter
import com.example.catalogofthings.presenter.adapters.TagSpinnerAdapter
import com.example.catalogofthings.presenter.viewModels.FolderViewModel
import com.google.android.material.button.MaterialButton
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

class FolderFragment : Fragment(R.layout.fragment_open_folder) {

    private lateinit var tagSpinner: Spinner
    private lateinit var tagAdapter: TagSpinnerAdapter

    private val binding: FragmentOpenFolderBinding by viewBinding(FragmentOpenFolderBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: FolderViewModel by viewModels { viewModelFactory }

    private lateinit var adapter: ListNotesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val folderId = arguments?.getInt("id") ?: 0

        if (folderId > 0 && viewModel.currentFolder.value == null) {
            viewModel.setFolder(folderId)
        }

        if (folderId == -1) {
            val parentId = arguments?.getInt("parentId") ?: 0
            viewModel.createFolder(
                NoteEntity(
                    title = "",
                    description = "",
                    isFolder = true,
                    parentId = parentId
                )
            )
        }

        setFragmentResultListener("requestKey") {requestKey, bundle ->
            val folderId = bundle.getInt("folderId")
            if (folderId > 0) viewModel.setFolder(folderId)
        }

        tagSpinner = binding.includeSearchBarFolderFragment.tagSpinnerFilterInFolder

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
                onNoteLongClick = ::onNoteLongClick,
        )

        with(binding.includedRecyclerNotesFolderFragment.recyclerNotes) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FolderFragment.adapter
        }

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
            val parentId = viewModel.currentFolder.value?.note?.noteId ?: 0
            findNavController().navigate(
                R.id.action_folderFragment_self,
                bundleOf(
                    "id" to -1,
                    "parentId" to parentId
                )
            )
            true
        }

        binding.includeHeaderFolder.titleFolder.doAfterTextChanged {
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
            }
            else if (parent == 0) {
                viewModel.setFolder(parent)
            }
            findNavController().popBackStack()
        }

        binding.includeSearchBarFolderFragment.searchInFolderFragment.doAfterTextChanged {
            viewModel.setSearchFilter(it.toString())
        }

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            val noteEntities = notes?.map { it.note } ?: emptyList()
            adapter.submitList(noteEntities)
        }

        viewModel.currentFolder.observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.getNotes(it.note.noteId)
                binding.includeHeaderFolder.titleFolder.setText(it.note.title)
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

    fun onTagFilterClick(tag: TagEntity?) {
        viewModel.setTagFilter(tag?.tagId)
    }

    fun onNoteClick(note: NoteEntity) {
        val parent = viewModel.currentFolder.value?.note?.noteId
        val bundle = bundleOf(
            "id" to note.noteId,
            "parentId" to parent
        )
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

    fun onNoteLongClick(note: NoteEntity) {
        // TODO Показать контекстное меню / удаление / перемещение и т.п.
//                showNoteContextMenu(note)
    }

    fun updateFolder(title: String) {
        val thisFolder = viewModel.currentFolder.value?.note
        val newFolder = NoteEntity(
            title = title,
            description = "",
            parentId = thisFolder?.parentId ?: 0
        )

        viewModel.updateFolder(
            thisFolder ?: newFolder,
            newFolder
        )
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}