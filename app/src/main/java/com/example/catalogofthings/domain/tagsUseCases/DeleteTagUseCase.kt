package com.example.catalogofthings.domain.tagsUseCases

import com.example.catalogofthings.data.TagsRepository
import com.example.catalogofthings.data.model.TagEntity
import javax.inject.Inject

interface DeleteTagUseCase {
    suspend operator fun invoke(tagEntity: TagEntity): Int
}

class DeleteTagUseCaseImpl @Inject constructor(
    private val repository: TagsRepository
): DeleteTagUseCase {
    override suspend fun invoke(tagEntity: TagEntity): Int =
        repository.deleteTag(tagEntity)
}