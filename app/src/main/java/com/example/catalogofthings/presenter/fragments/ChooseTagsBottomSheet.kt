package com.example.catalogofthings.presenter.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.catalogofthings.R
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalogofthings.appComponent
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.databinding.ChooseTagsBottomModalBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import com.example.catalogofthings.presenter.adapters.ListTagsInNoteAdapter
import com.example.catalogofthings.presenter.viewModels.ChooseTagsBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class ChooseTagsBottomSheet : BottomSheetDialogFragment(R.layout.choose_tags_bottom_modal) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ChooseTagsBottomSheetViewModel by viewModels {viewModelFactory}

    private lateinit var adapter: ListTagsInNoteAdapter

    private var onTagClick: (tag: TagEntity) -> Unit = {}

    fun setOnTagClick(onClick: (tag: TagEntity) -> Unit) {
        this.onTagClick = onClick
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ChooseTagsBottomModalBinding.inflate(
            inflater,
            container,
            false)

        adapter = ListTagsInNoteAdapter(
            onTagClick = onTagClick, onTagLongClick = ::onLongTagClick
        )

        with(binding.recyclerChooseTagsBottomModal) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ChooseTagsBottomSheet.adapter
        }

        binding.createNewTag.setOnClickListener {
            findNavController().navigate(
                R.id.action_noteFragment_to_newTagFragment
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tags.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    fun onLongTagClick(tag: TagEntity) {
        TagActionsDialog.show(
            context = requireContext(),
            tag = tag,
            onDelete = {
                viewModel.deleteTag(tag)
            },
            onEdit = {
                findNavController().navigate(
                    R.id.action_noteFragment_to_newTagFragment,
                    bundleOf("id" to tag.tagId)
                )
            }
        )
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}