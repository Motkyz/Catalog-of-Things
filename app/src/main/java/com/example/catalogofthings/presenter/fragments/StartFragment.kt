package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
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
import com.example.catalogofthings.presenter.MainViewModel
import com.example.catalogofthings.presenter.adapters.ListNotesAdapter
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

class StartFragment: Fragment(R.layout.fragment_start_app) {
    private val binding: FragmentStartAppBinding by viewBinding(FragmentStartAppBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels {viewModelFactory}
    private lateinit var adapter : ListNotesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.createNote(NoteEntity(1, "Test", "123", 100000000L))
        viewModel.createNote(NoteEntity(2, "Test1", "123", 100000000L))
        viewModel.createNote(NoteEntity(3, "Test2", "123", 100000000L))


        adapter = ListNotesAdapter(
            onNoteClick = { note ->
                // TODO порверка на заметку/ папку через isFolder
                if (false) {
                    findNavController().navigate(
                        R.id.action_startFragment_to_folderFragment,
                        bundleOf(
                            "title" to note.title
//                            и список добавить
                        )
                    )
                } else {
                    findNavController().navigate(
                        R.id.action_startFragment_to_noteFragment,
                        bundleOf(
                            "noteTitle" to note.title,
                            "description" to note.description,
                            "date" to note.date
                        )
                    )
                }
            },
            onNoteLongClick = { note ->
                // Показать контекстное меню / удаление / перемещение и т.п.
//                showNoteContextMenu(note)
            },
        )

        with(binding.includedRecyclerNotesStartFragment.recyclerNotes) {
            this
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@StartFragment.adapter
        }

        viewModel.notes.observe(viewLifecycleOwner) { notesWithTags ->
            val noteEntities = notesWithTags?.map { it.note } ?: emptyList()  // маппим к NoteEntity
            adapter.submitList(noteEntities)
        }

    }

    override fun onAttach(context: Context) {
        val component = context.appComponent
        component.inject(this)
        super.onAttach(context)
    }
}