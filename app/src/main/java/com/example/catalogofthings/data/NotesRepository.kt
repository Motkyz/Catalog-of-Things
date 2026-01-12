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

interface NotesRepository {
    fun getNotes(id: Int = 0): Flow<List<NoteWithTags>>
    suspend fun getNote(id: Int): NoteWithTags?
    suspend fun getFullNote(id: Int): NoteFull?
    suspend fun updateNote(oldNote: NoteEntity, newNote: NoteEntity)
    suspend fun createNote(noteEntity: NoteEntity): Int
    suspend fun deleteNote(noteEntity: NoteEntity)
    suspend fun addTag(noteId: Int, tagId: Int)
    suspend fun deleteTag(noteId: Int, tagId: Int)
    suspend fun addImage(noteId: Int, imageId: Int)
    suspend fun deleteImage(noteId: Int, imageId: Int)
    suspend fun updateChildrenCount(parentId: Int)
}

class NotesRepositoryImpl @Inject constructor(
    private val dao: NotesDAO
): NotesRepository {
    override fun getNotes(id: Int): Flow<List<NoteWithTags>> {
        return dao.getNotes(id)
    }

    override suspend fun getNote(id: Int): NoteWithTags? =
        dao.getNote(id)

    override suspend fun getFullNote(id: Int): NoteFull? =
        dao.getFullNote(id)

    override suspend fun updateNote(
        oldNote: NoteEntity,
        newNote: NoteEntity
    ) {
        dao.upsertNote(
            oldNote.copy(
                title = newNote.title,
                description = newNote.description,
                date = newNote.date,
                parentId = newNote.parentId
            )
        )

        if (oldNote.parentId != newNote.parentId) {
            updateChildrenCount(oldNote.parentId)
            updateChildrenCount(newNote.parentId)
        }
    }

    override suspend fun createNote(noteEntity: NoteEntity): Int {
        val noteId = dao.upsertNote(
            noteEntity
        ).toInt()

        updateChildrenCount(noteEntity.parentId)

        return noteId
    }

    override suspend fun deleteNote(noteEntity: NoteEntity) {
        dao.deleteNote(noteEntity)
        updateChildrenCount(noteEntity.parentId)
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

    override suspend fun deleteImage(
        noteId: Int,
        imageId: Int
    ) {
        dao.deleteNoteImage(
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
}