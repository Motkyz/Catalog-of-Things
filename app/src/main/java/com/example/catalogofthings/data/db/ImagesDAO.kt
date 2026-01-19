package com.example.catalogofthings.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.catalogofthings.data.model.ImageEntity

@Dao
interface ImagesDAO {

    @Upsert
    suspend fun upsertImage(imageEntity: ImageEntity): Long

    @Delete
    suspend fun deleteImage(imageEntity: ImageEntity): Int

    @Query("SELECT * FROM ${ImageEntity.TABLE} WHERE imageId = :id")
    suspend fun getImage(id: Int): ImageEntity?
}