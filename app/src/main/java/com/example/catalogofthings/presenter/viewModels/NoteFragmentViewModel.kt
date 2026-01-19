package com.example.catalogofthings.presenter.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogofthings.data.model.ImageEntity
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteFull
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.domain.imagesUseCases.CreateImageUseCase
import com.example.catalogofthings.domain.imagesUseCases.DeleteImageUseCase
import com.example.catalogofthings.domain.notesUseCases.AddTagToNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.CreateNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.DeleteTagFromNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.GetFullNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.UpdateNoteUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteFragmentViewModel @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase,
    private val getFullNoteUseCase: GetFullNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val createImageUseCase: CreateImageUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
    private val addTagToNoteUseCase: AddTagToNoteUseCase,
    private val deleteTagFromNoteUseCase: DeleteTagFromNoteUseCase,
): ViewModel() {

    private val _note = MutableLiveData<NoteFull?>()
    val note: LiveData<NoteFull?>
        get() = _note
    fun getNote(id: Int) {
        viewModelScope.launch {
            val fullNote = getFullNoteUseCase(id)
            _note.postValue(
                fullNote
            )

            val list = fullNote?.images ?: listOf()
            _noteImages.postValue(
                list
            )

            val tags = fullNote?.tags ?: listOf()
            _noteTags.postValue(
                tags
            )
        }
    }

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success

    fun createNote(
        noteEntity: NoteEntity
        ) {
        try{
            viewModelScope.launch {
                val noteId = createNoteUseCase(noteEntity)
                for (image in noteImages.value ?: listOf()) {
                    image.noteId = noteId
                    createImageUseCase(image)
                }
                for (tag in _noteTags.value ?: listOf()) {
                    addTagToNoteUseCase(noteId, tag.tagId)
                }

                _success.postValue(true)
            }
        } catch (e: Exception) {
            _success.postValue(false)
            throw e
        }
    }

    fun updateNote(
        oldNote: NoteEntity,
        newNote: NoteEntity
    ) {
        viewModelScope.launch {
            updateNoteUseCase(oldNote, newNote)

            val note = getFullNoteUseCase(oldNote.noteId)
            val oldTags = note?.tags ?: listOf()
            for (tag in oldTags) {
                if (!(_noteTags.value ?: listOf()).contains(tag))
                    deleteTagFromNoteUseCase(oldNote.noteId, tag.tagId)
            }
            for (tag in _noteTags.value ?: listOf()) {
                if (!oldTags.contains(tag))
                    addTagToNoteUseCase(oldNote.noteId, tag.tagId)
            }

            val oldImages = note?.images ?: listOf()
            for (image in noteImages.value ?: listOf()) {
                if (!oldImages.contains(image)) {
                    image.noteId = oldNote.noteId
                    createImageUseCase(image)
                }
            }

            _success.postValue(true)
        }
    }

    private val _noteTags = MutableLiveData<List<TagEntity>>()
    val noteTags: LiveData<List<TagEntity>>
        get() = _noteTags

    fun updateTags(newTags : List<TagEntity>){
        _noteTags.postValue(newTags)
    }

    private val _noteImages = MutableLiveData<List<ImageEntity>>()
    val noteImages: LiveData<List<ImageEntity>>
        get() = _noteImages

    fun addImage(image: ImageEntity) {
        viewModelScope.launch {
            val updatedList = _noteImages.value ?: emptyList()
            _noteImages.postValue(updatedList + image)
        }
    }

    fun deleteImage(image: ImageEntity){
        viewModelScope.launch {
            deleteImageUseCase(image)
            val updatedList = _noteImages.value ?: emptyList()
            _noteImages.postValue(updatedList - image)
        }
    }
}