package com.example.catalogofthings.domain.imagesUseCases

import com.example.catalogofthings.data.ImagesRepository
import com.example.catalogofthings.data.model.ImageEntity
import javax.inject.Inject

interface CreateImageUseCase {
    suspend operator fun invoke(imageEntity: ImageEntity): Int
}

class CreateImageUseCaseImpl @Inject constructor(
    private val repository: ImagesRepository
): CreateImageUseCase {
    override suspend fun invoke(imageEntity: ImageEntity): Int =
        repository.createImage(imageEntity)

}