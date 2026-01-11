package com.example.catalogofthings.domain.tagsUseCases

import com.example.catalogofthings.data.TagsRepository
import com.example.catalogofthings.data.model.TagEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetTagsUseCase {
    operator fun invoke(): Flow<List<TagEntity>>
}

class GetTagsUseCaseImpl @Inject constructor(
    private val repository: TagsRepository
): GetTagsUseCase {
    override fun invoke(): Flow<List<TagEntity>> {
        return repository.getAllTags()
    }
}