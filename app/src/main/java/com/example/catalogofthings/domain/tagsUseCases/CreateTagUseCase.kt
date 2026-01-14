package com.example.catalogofthings.domain.tagsUseCases

import com.example.catalogofthings.data.TagsRepository
import com.example.catalogofthings.data.model.TagEntity
import javax.inject.Inject

interface CreateTagUseCase {
    suspend operator fun invoke(tagEntity: TagEntity)
}

class CreateTagUseCaseImpl @Inject constructor(
    private val repository: TagsRepository
): CreateTagUseCase {
    override suspend fun invoke(tagEntity: TagEntity) {
        repository.createTag(tagEntity)
    }
}