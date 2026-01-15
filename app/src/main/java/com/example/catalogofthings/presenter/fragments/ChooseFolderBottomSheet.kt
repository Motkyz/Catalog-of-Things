package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.util.copy
import com.example.catalogofthings.R
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.databinding.ChooseFolderBottomModalBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import com.example.catalogofthings.presenter.adapters.ListNotesAdapter
import com.example.catalogofthings.presenter.viewModels.ChooseFolderBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class ChooseFolderBottomSheet : BottomSheetDialogFragment(R.layout.choose_folder_bottom_modal) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ChooseFolderBottomSheetViewModel by viewModels {viewModelFactory}

    private lateinit var adapter: ListNotesAdapter

    private var _note : NoteEntity? = null
    fun setNote(note : NoteEntity){
        _note = note
    }

    private var onFolderClick: (folder: NoteEntity) -> Unit = {}

    fun setOnFolderClick(onClick: (folder: NoteEntity) -> Unit) {
        this.onFolderClick = onClick
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ChooseFolderBottomModalBinding.inflate(
            inflater,
            container,
            false)

        adapter = ListNotesAdapter(
            onNoteClick = onFolderClick
        )

        with(binding.recyclerChooseFolderBottomModal) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ChooseFolderBottomSheet.adapter
        }

        binding.replaceInStart.setOnClickListener {
            viewModel.updateFolder(_note!!, _note!!.copy(parentId = 0))
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFolders(_note?.noteId ?: 0)

        viewModel.folders.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }


    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}