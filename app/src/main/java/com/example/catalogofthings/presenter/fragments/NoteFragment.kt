package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalogofthings.R
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.databinding.FragmentNoteBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import com.example.catalogofthings.presenter.MainViewModel
import com.example.catalogofthings.presenter.adapters.ListTagsInNoteAdapter
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject
import kotlin.getValue

class NoteFragment : Fragment() {
    private val binding: FragmentNoteBinding by viewBinding(FragmentNoteBinding::bind)

    // TODO у каждого фрагмента должна быть своя viewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels {viewModelFactory}
    private lateinit var adapter : ListTagsInNoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = ListTagsInNoteAdapter(
            onTagClick = { tag ->
                findNavController().navigate(
                    R.id.action_noteFragment_to_chooseTagBottomSheet,
//                    bundleOf()
                )
            }
        )

        with(binding.recyclerTagsNotes) {
            this
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@NoteFragment.adapter
        }

    }

    override fun onAttach(context: Context) {
        val component = context.appComponent
        component.inject(this)
        super.onAttach(context)
    }
}