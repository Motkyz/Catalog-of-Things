package com.example.catalogofthings.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogofthings.data.model.ImageEntity
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteFull
import com.example.catalogofthings.data.model.NoteWithTags
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.domain.imagesUseCases.CreateImageUseCase
import com.example.catalogofthings.domain.imagesUseCases.GetImageUseCase
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    private val deleteImageFromNoteUseCase: DeleteImageFromNoteUseCase,
    private val createImageUseCase: CreateImageUseCase,
    private val getImageUseCase: GetImageUseCase,
    ): ViewModel() {

    private val _notes = MutableLiveData<List<NoteWithTags>>()
    val notes: LiveData<List<NoteWithTags>>
        get() = _notes

    init {
        getNotes(0)
    }

    fun getNotes(id: Int) {
        viewModelScope.launch {
            getNotesUseCase(id).collect {
                _notes.postValue(it)
            }
        }
    }

    private val _image = MutableLiveData<ImageEntity>()
    val image : LiveData<ImageEntity>
        get() = _image

    private suspend fun getImage(id : Int) : ImageEntity? {
        return getImageUseCase(id)
    }

    private val _imagesData = MutableLiveData<List<ImageEntity>>()
    val imagesData : LiveData<List<ImageEntity>>
        get() = _imagesData

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success
    fun createNote(
        noteEntity: NoteEntity) {
        try{
            viewModelScope.launch {
                val noteId = createNoteUseCase(noteEntity)
                Log.d("CNI", images.value.toString())
                for (image in _images.value ?: listOf()) {
                    addImageToNoteUseCase(noteId, image)
                }
                Log.d("CNT", _noteTags.value.toString())
                for (tag in _noteTags.value ?: listOf()) {
                    Log.d("CNTI", tag.toString())
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

            val oldImages = note?.images?.map { it.imageId } ?: listOf()
            for (image in oldImages) {
                if (!(_images.value ?: listOf()).contains(image))
                    deleteImageFromNoteUseCase(oldNote.noteId, image)
            }
            for (image in _images.value ?: listOf()) {
                if (!oldImages.contains(image)) {
                    addImageToNoteUseCase(oldNote.noteId, image)
                }
            }
            _success.postValue(true)
        }
    }

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
            _images.postValue(
                list.map { it.imageId }
            )
            _imagesData.postValue(
                list
            )

            val tags = fullNote?.tags ?: listOf()
            _noteTags.postValue(
                tags
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

    private val _images = MutableLiveData<List<Int>>()
    val images : LiveData<List<Int>>
        get() = _images

    fun loadImage(image : ImageEntity){
        viewModelScope.launch {
            val imageId = createImageUseCase(image)
            val currentList = _images.value ?: emptyList()
            _images.postValue(currentList + imageId)

            val list = _imagesData.value ?: emptyList()
            _imagesData.postValue(list + image)
        }
    }

    fun addImage(noteId: Int, imageId: Int) {
        viewModelScope.launch {
            addImageToNoteUseCase(noteId, imageId)
        }
    }

    private val _noteTags = MutableLiveData<List<TagEntity>>()
    val noteTags: LiveData<List<TagEntity>>
        get() = _noteTags

    fun addTagToList(tag: TagEntity) {
        viewModelScope.launch {
            val currentList = _noteTags.value ?: emptyList()
            _noteTags.postValue(currentList + tag)
        }
    }

}