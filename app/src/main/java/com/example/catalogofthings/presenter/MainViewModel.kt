package com.example.catalogofthings.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogofthings.data.model.ImageEntity
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteFull
import com.example.catalogofthings.data.model.NoteWithTags
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.domain.notesUseCases.AddImageToNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.CreateNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.GetNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.GetNotesUseCase
import com.example.catalogofthings.domain.notesUseCases.AddTagToNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.DeleteImageFromNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.DeleteTagFromNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.GetFullNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.UpdateNoteUseCase
import com.example.catalogofthings.domain.tagsUseCases.CreateTagUseCase
import com.example.catalogofthings.domain.tagsUseCases.GetTagsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val createNoteUseCase: CreateNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val getFullNoteUseCase: GetFullNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val createTagUseCase: CreateTagUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val addTagToNoteUseCase: AddTagToNoteUseCase,
    private val deleteTagFromNoteUseCase: DeleteTagFromNoteUseCase,
    private val addImageToNoteUseCase: AddImageToNoteUseCase,
    private val deleteImageFromNoteUseCase: DeleteImageFromNoteUseCase
): ViewModel() {

    private val _notes = MutableLiveData<List<NoteWithTags>>()
    val notes: LiveData<List<NoteWithTags>>
        get() = _notes

    init {
        getNotes(0)
        getTags()
    }

    fun createNote(
        noteEntity: NoteEntity,
        tags: List<TagEntity> = listOf(),
        images: List<ImageEntity> = listOf()
    ) {
        viewModelScope.launch {
            val noteId = createNoteUseCase(noteEntity)
            for (tag in tags) {
                addTagToNoteUseCase(noteEntity.noteId, tag.tagId)
            }
            for (image in images) {
                addImageToNoteUseCase(noteEntity.noteId, image.imageId)
            }
        }
    }

    fun getNotes(id: Int) {
        viewModelScope.launch {
            getNotesUseCase(id).collect {
                _notes.postValue(it)
            }
        }
    }

    fun updateNote(
        oldNote: NoteEntity,
        newNote: NoteEntity,
        tags: List<TagEntity> = listOf(),
        images: List<ImageEntity> = listOf()
    ) {
        viewModelScope.launch {
            updateNoteUseCase(oldNote, newNote)
            val note = getFullNoteUseCase(oldNote.noteId)
            val oldTags = note?.tags ?: listOf()
            for (tag in oldTags) {
                if (!tags.contains(tag))
                    deleteTagFromNoteUseCase(oldNote.noteId, tag.tagId)
            }
            for (tag in tags) {
                if (!oldTags.contains(tag))
                    addTagToNoteUseCase(oldNote.noteId, tag.tagId)
            }

            val oldImages = note?.images ?: listOf()
            for (image in oldImages) {
                if (!images.contains(image))
                    deleteImageFromNoteUseCase(oldNote.noteId, image.imageId)
            }
            for (image in images) {
                if (!oldImages.contains(image))
                    addImageToNoteUseCase(oldNote.noteId, image.imageId)
            }
        }
    }

    private val _note = MutableLiveData<NoteFull?>()
    val note: LiveData<NoteFull?>
        get() = _note
    fun getNote(id: Int) {
        viewModelScope.launch {
            _note.postValue(
                getFullNoteUseCase(id)
            )
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
}