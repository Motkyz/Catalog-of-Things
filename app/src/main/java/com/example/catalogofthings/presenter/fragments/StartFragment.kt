package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalogofthings.R
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.databinding.FragmentStartAppBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import com.example.catalogofthings.presenter.adapters.ListNotesAdapter
import com.example.catalogofthings.presenter.viewModels.FolderViewModel
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

class StartFragment: Fragment(R.layout.fragment_start_app) {
    private val binding: FragmentStartAppBinding by viewBinding(FragmentStartAppBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: FolderViewModel by viewModels {viewModelFactory}
    private lateinit var adapter : ListNotesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNotes(0)

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
            viewModel.createFolder(
                NoteEntity(
                    title = "",
                    description = "",
                    isFolder = true,
                    parentId = 0
                )
            )
            true
        }

        viewModel.notes.observe(viewLifecycleOwner) { notesWithTags ->
            val noteEntities = notesWithTags?.map { it.note } ?: emptyList()
            adapter.submitList(noteEntities)
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

    fun onNoteLongClick(note: NoteEntity) {
        // TODO Показать контекстное меню / удаление / перемещение и т.п.
//                showNoteContextMenu(note)
    }

    override fun onAttach(context: Context) {
        val component = context.appComponent
        component.inject(this)
        super.onAttach(context)
    }
}