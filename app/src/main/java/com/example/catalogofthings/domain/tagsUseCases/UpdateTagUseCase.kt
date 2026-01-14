package com.example.catalogofthings.domain.tagsUseCases

import com.example.catalogofthings.data.TagsRepository
import com.example.catalogofthings.data.model.TagEntity
import javax.inject.Inject

interface UpdateTagUseCase {
    suspend operator fun invoke(oldTag: TagEntity, newTag: TagEntity)
}

class UpdateTagUseCaseImpl @Inject constructor(
    private val repository: TagsRepository
): UpdateTagUseCase {
    override suspend fun invoke(
        oldTag: TagEntity,
        newTag: TagEntity
    ) {
        repository.updateTag(oldTag, newTag)
    }
}