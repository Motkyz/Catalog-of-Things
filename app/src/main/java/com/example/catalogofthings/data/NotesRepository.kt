package com.example.catalogofthings.data

import android.util.Log
import com.example.catalogofthings.data.db.NotesDAO
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteFull
import com.example.catalogofthings.data.model.NoteImageCrossRef
import com.example.catalogofthings.data.model.NoteWithTags
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.catalogofthings.data.model.NoteTagCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface NotesRepository {
    fun getNotes(id: Int = 0): Flow<List<NoteWithTags>>
    fun getNotesByFilters(search: String?, tagId: Int?): Flow<List<NoteWithTags>>
    suspend fun getNote(id: Int): NoteWithTags?
    fun getAllFolders(id : Int): Flow<List<NoteEntity>>
    suspend fun getFullNote(id: Int): NoteFull?
    suspend fun updateNote(oldNote: NoteEntity, newNote: NoteEntity)
    suspend fun createNote(noteEntity: NoteEntity): Int
    suspend fun deleteNote(noteEntity: NoteEntity): Int
    suspend fun addTag(noteId: Int, tagId: Int)
    suspend fun deleteTag(noteId: Int, tagId: Int)
    suspend fun addImage(noteId: Int, imageId: Int)
    suspend fun updateChildrenCount(parentId: Int)
}

class NotesRepositoryImpl @Inject constructor(
    private val dao: NotesDAO
): NotesRepository {
    override fun getNotes(id: Int): Flow<List<NoteWithTags>> =
        dao.getNotes(id)

    override fun getNotesByFilters(
        search: String?,
        tagId: Int?
    ): Flow<List<NoteWithTags>> {
        return if (!search.isNullOrBlank() && tagId != null) {
            dao.getNotesByFilters(search, tagId)
        } else if (tagId != null) {
            dao.getNotesByTag(tagId)
        } else {
            dao.getNotesBySearch(search ?: "")
        }
    }

    override suspend fun getNote(id: Int): NoteWithTags? =
        dao.getNote(id)

    override fun getAllFolders(id : Int): Flow<List<NoteEntity>> {
        val notChildrenFolders = dao.getAllFolders().map {list ->
            list.filter {folder ->
                !folder.location.split("/").contains(id.toString())
                        && folder.noteId != id
            }
        }

        return notChildrenFolders
    }

    override suspend fun getFullNote(id: Int): NoteFull? =
        dao.getFullNote(id)

    override suspend fun updateNote(
        oldNote: NoteEntity,
        newNote: NoteEntity
    ) {
        //Получаем "ссылку" на местоположение
        val location = buildLocation(getParent(newNote.parentId))

        dao.upsertNote(
            oldNote.copy(
                title = newNote.title,
                description = newNote.description,
                date = newNote.date,
                location = location,
                parentId = newNote.parentId,
                icon = newNote.icon
            )
        )

        if (oldNote.parentId != newNote.parentId) {
            if (oldNote.parentId != 0) updateChildrenCount(oldNote.parentId)
            if (newNote.parentId != 0) updateChildrenCount(newNote.parentId)
        }
    }

    override suspend fun createNote(noteEntity: NoteEntity): Int {
        //Получаем "ссылку" на местоположение
        val parentNote = getParent(noteEntity.parentId)
        val location = buildLocation(parentNote)
        noteEntity.location = location

        val noteId = dao.upsertNote(
            noteEntity
        ).toInt()

        updateChildrenCount(noteEntity.parentId)

        return noteId
    }

    override suspend fun deleteNote(noteEntity: NoteEntity): Int {
        //Удаляем дочерние элементы
        if (noteEntity.isFolder) {
            val children = dao.getNotes(noteEntity.noteId).first()
            for (child in children) {
                deleteNote(child.note)
            }
        } else {
            //Удаляем картинки из бд
            val fullNote = getFullNote(noteEntity.noteId)
            for (image in fullNote?.images ?: listOf()) {
                dao.deleteImage(image)
            }
        }

        val id = dao.deleteNote(noteEntity)
        updateChildrenCount(noteEntity.parentId)
        return id
    }

    override suspend fun addTag(
        noteId: Int,
        tagId: Int
    ) {
        dao.addNoteTag(
            NoteTagCrossRef(
                noteId,
                tagId
            )
        )
    }

    override suspend fun deleteTag(
        noteId: Int,
        tagId: Int
    ) {
        dao.deleteNoteTag(
            NoteTagCrossRef(
                noteId,
                tagId
            )
        )
    }

    override suspend fun addImage(
        noteId: Int,
        imageId: Int
    ) {
        Log.d("addImage", "$noteId, $imageId")
        dao.addNoteImage(
            NoteImageCrossRef(
                noteId,
                imageId
            )
        )
    }

    override suspend fun updateChildrenCount(parentId: Int) {
        if (parentId != 0) {
            val count = dao.getChildrenCount(parentId)
            val note = dao.getNoteWithOutTags(parentId)
            if (note != null) {
                dao.upsertNote(
                    note.copy(
                        childrenCount = count
                    )
                )
            }
        }
    }

    private suspend fun getParent(id: Int): NoteEntity? =
        dao.getNoteWithOutTags(id)

    private fun buildLocation(parentNote: NoteEntity?): String =
        "${parentNote?.location ?: ""}${parentNote?.noteId ?: 0}/"
}