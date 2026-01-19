package com.example.catalogofthings.presenter.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catalogofthings.R
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.databinding.IncludeNotesNotFoundBinding
import com.example.catalogofthings.databinding.IncludeRecyclerNotesBinding
import com.example.catalogofthings.databinding.IncludeSearchBarBinding
import com.example.catalogofthings.di.viewModel.ViewModelFactory
import com.example.catalogofthings.enums.SortingVariantsEnum
import com.example.catalogofthings.presenter.adapters.ListNotesAdapter
import com.example.catalogofthings.presenter.adapters.SortingSpinnerAdapter
import com.example.catalogofthings.presenter.adapters.TagSpinnerAdapter
import com.example.catalogofthings.presenter.viewModels.FolderViewModel
import com.google.android.material.button.MaterialButton
import javax.inject.Inject
import kotlin.getValue

abstract class BaseFolderFragment(fragment: Int) : Fragment(fragment) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected val viewModel: FolderViewModel by viewModels { viewModelFactory }

    protected abstract val tagSpinner: Spinner?
    protected abstract val variantSpinner: Spinner?

    protected abstract val bindingNotesNotFound: IncludeNotesNotFoundBinding?
    protected abstract val bindingRecyclerNotes: IncludeRecyclerNotesBinding?
    protected abstract val bindingSearchBar: IncludeSearchBarBinding?

    protected lateinit var adapter: ListNotesAdapter
    protected lateinit var tagAdapter: TagSpinnerAdapter
    protected lateinit var variantAdapter: SortingSpinnerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.getTags()
    }

    protected open fun setBindings() {

        bindingSearchBar?.searchInFolderFragment?.doAfterTextChanged {
            viewModel.setSearchFilter(it.toString())
        }
    }

    protected open fun setObserves() {

        viewModel.notes.observe(viewLifecycleOwner) { notesWithTags ->
            val notes = notesWithTags ?: emptyList()
            val noteEntities = notes.map { it.note }

            if (notes.isEmpty()) {
                bindingNotesNotFound?.root?.visibility = View.VISIBLE
                bindingRecyclerNotes?.recyclerNotes?.visibility = View.GONE
            } else {
                bindingNotesNotFound?.root?.visibility = View.GONE
                bindingRecyclerNotes?.recyclerNotes?.visibility = View.VISIBLE

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
            tagSpinner?.setSelection(tagAdapter.getPosition(it))

            closeSpinner(tagSpinner)
        }

        viewModel.sortingDirection.observe(viewLifecycleOwner) {
            with(bindingSearchBar) {
                if (it)
                    this?.sortingIcon?.setImageResource(R.drawable.ic_sort_asc)
                else
                    this?.sortingIcon?.setImageResource(R.drawable.ic_sort_desc)
            }

            closeSpinner(variantSpinner)
        }

        viewModel.sortingVariant.observe(viewLifecycleOwner) {
            variantSpinner?.setSelection(variantAdapter.getPosition(it))
            val thisFolderId = viewModel.currentFolder.value?.note?.noteId
            viewModel.getNotes(thisFolderId)

            closeSpinner(variantSpinner)
        }
    }

    protected open fun initMainAdapter() {
        adapter = ListNotesAdapter(
            onNoteClick = ::onNoteClick,
            onNoteLongClick = ::onNoteLongClick
        )

        with(bindingRecyclerNotes?.recyclerNotes) {
            this?.layoutManager = LinearLayoutManager(requireContext())
            this?.adapter = this@BaseFolderFragment.adapter
        }
    }
    protected open fun initTagSpinner() {
        tagAdapter = TagSpinnerAdapter(requireContext(), showEmptyOption = true)
        tagAdapter.setOnTagFilterClick(::onTagFilterClick)
        tagSpinner?.adapter = tagAdapter

        tagSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
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

    protected open fun initVariantSpinner() {
        variantAdapter = SortingSpinnerAdapter(requireContext())
        variantAdapter.setOnVariantClick(::onVariantClick)
        variantSpinner?.adapter = variantAdapter

        variantSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedVariant = variantAdapter.getItem(position)

                if (view is MaterialButton) {
                    if (selectedVariant == null) {
                        view.text = SortingVariantsEnum.ON_CREATE_DATE.variant
                    } else {
                        view.text = selectedVariant.variant
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun onTagFilterClick(tag: TagEntity?) {
        viewModel.setTagFilter(tag?.tagId)
    }

    fun onVariantClick(variant: SortingVariantsEnum?) {
        viewModel.setSort(variant ?: SortingVariantsEnum.ON_CREATE_DATE)
    }

    protected abstract fun onNoteClick(note: NoteEntity)
    protected abstract fun onNoteLongClick(note: NoteEntity)

    protected fun closeSpinner(spinner: Spinner?) {
        val method = Spinner::class.java.getDeclaredMethod(
            "onDetachedFromWindow"
        )
        method.isAccessible = true
        method.invoke(spinner)
    }
}