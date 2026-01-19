package com.example.catalogofthings.data

import com.example.catalogofthings.data.db.ImagesDAO
import com.example.catalogofthings.data.model.ImageEntity
import javax.inject.Inject

interface ImagesRepository {
    suspend fun createImage(imageEntity: ImageEntity): Int
    suspend fun deleteImage(imageEntity: ImageEntity): Int
    suspend fun getImage(id: Int) : ImageEntity?
}

class ImagesRepositoryImpl @Inject constructor(
    private val dao: ImagesDAO
): ImagesRepository {
    override suspend fun createImage(imageEntity: ImageEntity): Int =
        dao.upsertImage(
            imageEntity
        ).toInt()

    override suspend fun deleteImage(imageEntity: ImageEntity): Int =
        dao.deleteImage(
            imageEntity
        )

    override suspend fun getImage(id: Int): ImageEntity? =
        dao.getImage(id)
}