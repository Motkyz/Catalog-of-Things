package com.example.catalogofthings.data

import com.example.catalogofthings.data.db.NotesDAO
import com.example.catalogofthings.data.model.TagEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TagsRepository {
    suspend fun createTag(tagEntity: TagEntity): Int
    fun getAllTags(): Flow<List<TagEntity>>
    suspend fun updateTag(tagEntityOld: TagEntity, tagEntityNew: TagEntity)
    suspend fun deleteTag(tagEntity: TagEntity): Int
}

class TagsRepositoryImpl @Inject constructor(
    private val dao: NotesDAO
): TagsRepository {

    override suspend fun createTag(tagEntity: TagEntity): Int =
        dao.upsertTag(
            tagEntity
        ).toInt()

    override fun getAllTags(): Flow<List<TagEntity>> =
        dao.getAllTags()

    override suspend fun updateTag(
        tagEntityOld: TagEntity,
        tagEntityNew: TagEntity
    ) {
        dao.upsertTag(
            tagEntityOld.copy(
                title = tagEntityNew.title,
                color = tagEntityNew.color
            )
        )
    }

    override suspend fun deleteTag(tagEntity: TagEntity): Int =
        dao.deleteTag(tagEntity)
}