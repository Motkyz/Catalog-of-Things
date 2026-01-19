package com.example.catalogofthings.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.catalogofthings.data.model.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDAO {

    @Upsert
    suspend fun upsertTag(tagEntity: TagEntity): Long

    @Query("SELECT * FROM ${TagEntity.TABLE} ORDER BY title")
    fun getAllTags(): Flow<List<TagEntity>>

    @Query("SELECT * FROM ${TagEntity.TABLE} WHERE tagId = :id")
    suspend fun getTag(id: Int): TagEntity?

    @Delete
    suspend fun deleteTag(tagEntity: TagEntity): Int
}