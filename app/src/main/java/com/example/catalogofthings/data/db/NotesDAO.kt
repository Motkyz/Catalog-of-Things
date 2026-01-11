package com.example.catalogofthings.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteTagCrossRef
import com.example.catalogofthings.data.model.NoteWithTags
import com.example.catalogofthings.data.model.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDAO {

    //Notes
    @Upsert
    suspend fun upsertNote(noteEntity: NoteEntity): Long

    @Transaction
    @Query("SELECT * FROM ${NoteEntity.TABLE} WHERE parentId = :parentId ORDER BY noteId")
    fun getNotes(parentId: Int = 0): Flow<List<NoteWithTags>>

    @Transaction
    @Query("SELECT * FROM ${NoteEntity.TABLE} WHERE noteId = :id")
    suspend fun getNote(id: Int): NoteWithTags?

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)

    //Tags
    @Upsert
    suspend fun upsertTag(tagEntity: TagEntity)

    @Query("SELECT * FROM ${TagEntity.TABLE} ORDER BY title")
    fun getAllTags(): Flow<List<TagEntity>>

    @Delete
    suspend fun deleteTag(tagEntity: TagEntity)

    //NoteTags
    @Upsert
    suspend fun addNoteTag(noteTagCrossRef: NoteTagCrossRef)

    @Delete
    suspend fun deleteNoteTag(noteTagCrossRef: NoteTagCrossRef)

    @Query("DELETE FROM ${NoteTagCrossRef.TABLE} WHERE noteId=:noteId")
    suspend fun deleteNoteTags(noteId: Int)
}