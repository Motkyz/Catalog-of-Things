package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.os.bundleOf
import com.example.catalogofthings.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.databinding.FragmentOpenFolderBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import com.example.catalogofthings.presenter.MainViewModel
import com.example.catalogofthings.presenter.adapters.ListNotesAdapter
import com.example.catalogofthings.presenter.viewModels.FolderViewModel
import dev.androidbroadcast.vbpd.viewBinding
import java.lang.NullPointerException
import javax.inject.Inject

class FolderFragment : Fragment(R.layout.fragment_open_folder) {

    private val binding: FragmentOpenFolderBinding by viewBinding(FragmentOpenFolderBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: FolderViewModel by viewModels { viewModelFactory }

    private lateinit var adapter: ListNotesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val folderId = arguments?.getInt("id") ?: 0

        if (folderId > 0) {
            viewModel.setFolder(folderId)
        }

        adapter = ListNotesAdapter(
                onNoteClick = ::onNoteClick,
                onNoteLongClick = ::onNoteLongClick,
        )

        with(binding.includedRecyclerNotesFolderFragment.recyclerNotes) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FolderFragment.adapter
        }

        binding.addNew.setOnClickListener {
            findNavController().navigate(
                R.id.action_folderFragment_to_noteFragment,
                bundleOf("id" to "0")
            )
        }

        binding.addNew.setOnLongClickListener {
            viewModel.createFolder(
                NoteEntity(
                    title = "",
                    description = "",
                    isFolder = true,
                    parentId = viewModel.currentFolder.value?.note?.noteId ?: 0
                )
            )
            true
        }

        binding.includeHeaderFolder.icBackArrow.setOnClickListener {
            val parent = viewModel.currentFolder.value?.note?.parentId
            if (parent != null && parent != 0) {
                viewModel.setFolder(parent)
            }
            else if (parent == 0) {
                findNavController().popBackStack()
                viewModel.setFolder(0)
            }
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
    }

    fun onNoteClick(note: NoteEntity) {
        if (note.isFolder) {
            viewModel.setFolder(note.noteId)
        } else {
            findNavController().navigate(
                R.id.action_folderFragment_to_noteFragment,
                bundleOf(
                    "id" to note.noteId.toString()
                )
            )
        }
    }

    fun onNoteLongClick(note: NoteEntity) {
        // TODO Показать контекстное меню / удаление / перемещение и т.п.
//                showNoteContextMenu(note)
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}