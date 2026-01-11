package com.example.catalogofthings.data

import com.example.catalogofthings.data.db.NotesDAO
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteWithTags
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.catalogofthings.data.model.NoteTagCrossRef

interface NotesRepository {
    fun getNotes(id: Int = 0): Flow<List<NoteWithTags>>
    suspend fun getNote(id: Int): NoteWithTags?
    suspend fun updateNote(oldNote: NoteEntity, newNote: NoteEntity)
    suspend fun createNote(noteEntity: NoteEntity): Int
    suspend fun addTag(noteId: Int, tagId: Int)
    suspend fun deleteTag(noteId: Int, tagId: Int)
}

class NotesRepositoryImpl @Inject constructor(
    private val dao: NotesDAO
): NotesRepository {
    override fun getNotes(id: Int): Flow<List<NoteWithTags>> {
        return dao.getNotes(id)
    }

    override suspend fun getNote(id: Int): NoteWithTags? {
        val note = dao.getNote(id)
        return note
    }

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
    }

    override suspend fun createNote(noteEntity: NoteEntity): Int =
        dao.upsertNote(
            noteEntity
        ).toInt()

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
}