package com.example.catalogofthings.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.catalogofthings.data.model.ImageEntity
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteFull
import com.example.catalogofthings.data.model.NoteImageCrossRef
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

    @Query("SELECT * FROM ${NoteEntity.TABLE} WHERE noteId = :id")
    suspend fun getNoteWithOutTags(id: Int): NoteEntity?

    @Transaction
    @Query("SELECT * FROM ${NoteEntity.TABLE} WHERE noteId = :id")
    suspend fun getNote(id: Int): NoteWithTags?

    @Transaction
    @Query("SELECT * FROM ${NoteEntity.TABLE} WHERE noteId = :id")
    suspend fun getFullNote(id: Int): NoteFull?

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity): Int

    @Query("SELECT COUNT() FROM ${NoteEntity.TABLE} WHERE parentId = :parentId")
    suspend fun getChildrenCount(parentId: Int): Int

    //Tags
    @Upsert
    suspend fun upsertTag(tagEntity: TagEntity): Long

    @Query("SELECT * FROM ${TagEntity.TABLE} ORDER BY title")
    fun getAllTags(): Flow<List<TagEntity>>

    @Delete
    suspend fun deleteTag(tagEntity: TagEntity): Int

    //Images
    @Upsert
    suspend fun upsertImage(imageEntity: ImageEntity): Long

    @Delete
    suspend fun deleteImage(imageEntity: ImageEntity): Int

    @Query("SELECT * FROM ${ImageEntity.TABLE} WHERE imageId = :id")
    suspend fun getImage(id: Int): ImageEntity?

    //NoteTags
    @Upsert
    suspend fun addNoteTag(noteTagCrossRef: NoteTagCrossRef)

    @Delete
    suspend fun deleteNoteTag(noteTagCrossRef: NoteTagCrossRef)

    @Query("DELETE FROM ${NoteTagCrossRef.TABLE} WHERE noteId = :noteId")
    suspend fun deleteNoteTags(noteId: Int)

    //NoteImages
    @Upsert
    suspend fun addNoteImage(noteImageCrossRef: NoteImageCrossRef)

    @Query("DELETE FROM ${NoteImageCrossRef.TABLE} WHERE noteId = :noteId")
    suspend fun deleteNoteImages(noteId: Int)
}