package com.example.catalogofthings.domain.imagesUseCases

import com.example.catalogofthings.data.ImagesRepository
import com.example.catalogofthings.data.model.ImageEntity
import javax.inject.Inject

interface DeleteImageUseCase {
    suspend operator fun invoke(imageEntity: ImageEntity): Int
}

class DeleteImageUseCaseImpl @Inject constructor(
    private val repository: ImagesRepository
): DeleteImageUseCase {
    override suspend fun invoke(imageEntity: ImageEntity): Int =
        repository.deleteImage(imageEntity)

}