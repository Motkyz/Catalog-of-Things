package com.example.catalogofthings.domain.tagsUseCases

import com.example.catalogofthings.data.TagsRepository
import com.example.catalogofthings.data.model.TagEntity
import javax.inject.Inject

interface GetTagUseCase {
    suspend operator fun invoke(id: Int): TagEntity?
}

class GetTagUseCaseImpl @Inject constructor(
    private val repository: TagsRepository
): GetTagUseCase {
    override suspend fun invoke(id: Int): TagEntity? =
        repository.getTag(id)
}