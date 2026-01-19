package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.databinding.CreateFolderBottomModalBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CreateFolderBottomSheet : BottomSheetDialogFragment() {

    private var _binding: CreateFolderBottomModalBinding? = null
    private val binding get() = _binding!!

    private var parentId: Int = 0
    private var onFolderCreated: ((NoteEntity) -> Unit) = {}

    companion object {
        private const val ARG_PARENT_ID = "arg_parent_id"

        fun newInstance(parentId: Int?): CreateFolderBottomSheet {
            return CreateFolderBottomSheet().apply {
                arguments = bundleOf(ARG_PARENT_ID to parentId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentId = arguments?.getInt(ARG_PARENT_ID) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateFolderBottomModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextForNameFolder.requestFocus()
        binding.editTextForNameFolder.post {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editTextForNameFolder, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.cancelCreateNewFolder.setOnClickListener {
            dismiss()
        }

        binding.createNewFolder.setOnClickListener {
            val title = binding.editTextForNameFolder.text?.toString()?.trim() ?: ""

            if (title.isBlank()) {
                binding.editTextForNameFolder.error = "Введите название папки"
                return@setOnClickListener
            }

            val newFolder = NoteEntity(
                title = title,
                description = "",
                isFolder = true,
                parentId = parentId
            )

            onFolderCreated(newFolder)
            dismiss()
        }
    }

    fun setOnFolderCreatedListener(listener: (NoteEntity) -> Unit) {
        this.onFolderCreated = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}