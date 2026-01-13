package com.example.catalogofthings.presenter.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteWithTags
import com.example.catalogofthings.domain.notesUseCases.CreateNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.GetNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.GetNotesUseCase
import com.example.catalogofthings.domain.notesUseCases.UpdateNoteUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class FolderViewModel @Inject constructor(
    private val getNoteUseCase: GetNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase,
    private val createNoteUseCase: CreateNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
): ViewModel() {

//    init {
//        getNotes(0)
//    }

    private val _currentFolder = MutableLiveData<NoteWithTags?>()
    val currentFolder: LiveData<NoteWithTags?>
        get() = _currentFolder

    fun setFolder(id: Int) {
        Log.d("Переход в папку", id.toString())
        viewModelScope.launch {
            val folder = getNoteUseCase(id)
            _currentFolder.postValue(
                folder
            )
            Log.d("Загрузил папку", folder.toString())
        }
    }

    private val _notes = MutableLiveData<List<NoteWithTags>>()
    val notes: LiveData<List<NoteWithTags>>
        get() = _notes

    fun getNotes(id: Int) {
        viewModelScope.launch {
            Log.d("Подгрузка заметок", id.toString())
            getNotesUseCase(id).collect {
                _notes.postValue(it)
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
}