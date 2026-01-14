package com.example.catalogofthings.domain.imagesUseCases

import com.example.catalogofthings.data.ImagesRepository
import com.example.catalogofthings.data.model.ImageEntity
import javax.inject.Inject

interface GetImageUseCase {
    suspend operator fun invoke(id: Int): ImageEntity?
}

class GetImageUseCaseImpl @Inject constructor(
    private val repository: ImagesRepository
): GetImageUseCase {
    override suspend fun invoke(id: Int): ImageEntity? =
        repository.getImage(id)
}