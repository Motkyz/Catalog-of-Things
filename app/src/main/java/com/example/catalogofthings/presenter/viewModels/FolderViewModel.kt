package com.example.catalogofthings.presenter.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteWithTags
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.domain.notesUseCases.CreateNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.DeleteNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.GetNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.GetNotesByFiltersUseCase
import com.example.catalogofthings.domain.notesUseCases.GetNotesUseCase
import com.example.catalogofthings.domain.notesUseCases.UpdateNoteUseCase
import com.example.catalogofthings.domain.tagsUseCases.GetTagsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class FolderViewModel @Inject constructor(
    private val getNoteUseCase: GetNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase,
    private val getNotesByFiltersUseCase: GetNotesByFiltersUseCase,
    private val createNoteUseCase: CreateNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
): ViewModel() {

    private val _searchFilter = MutableLiveData<String?>()
    val searchFilter: LiveData<String?>
        get() = _searchFilter

    fun setSearchFilter(filter: String?) {
        _searchFilter.postValue(
            filter
        )
    }

    private val _tagFilter = MutableLiveData<Int?>()
    val tagFilter: LiveData<Int?>
        get() = _tagFilter

    fun setTagFilter(filter: Int?) {
        _tagFilter.postValue(
            filter
        )
    }

    private val _currentFolder = MutableLiveData<NoteWithTags?>()
    val currentFolder: LiveData<NoteWithTags?>
        get() = _currentFolder

    fun setFolder(id: Int) {
        viewModelScope.launch {
            val folder = getNoteUseCase(id)
            _currentFolder.postValue(
                folder
            )
        }
    }

    private val _notes = MutableLiveData<List<NoteWithTags>>()
    val notes: LiveData<List<NoteWithTags>>
        get() = _notes

    fun getNotes(id: Int) {
        viewModelScope.launch {
            getNotesUseCase(id).collect {
                _notes.postValue(it)
            }
        }
    }

    fun getNotesByFilters() {
        if (searchFilter.value.isNullOrBlank() && tagFilter.value == null) {
            getNotes(currentFolder.value?.note?.noteId ?: 0)
        } else {
            viewModelScope.launch {
                getNotesByFiltersUseCase(
                    searchFilter.value, tagFilter.value
                ).collect {
                    _notes.postValue(it)
                }
            }
        }
    }

    private val _tags = MutableLiveData<List<TagEntity>>()
    val tags: LiveData<List<TagEntity>>
        get() = _tags

    fun getTags() {
        viewModelScope.launch {
            getTagsUseCase().collect {
                _tags.postValue(it)
            }
        }
    }

    fun createFolder(folder: NoteEntity) {
        viewModelScope.launch {
            val newFolder = createNoteUseCase(folder)
            setFolder(newFolder)
        }
    }

    fun updateFolder(oldFolder: NoteEntity, newFolder: NoteEntity) {
        viewModelScope.launch {
            updateNoteUseCase(oldFolder, newFolder)
        }
    }

    fun deleteNote(note : NoteEntity){
        viewModelScope.launch {
            deleteNoteUseCase(note)
        }
    }
}