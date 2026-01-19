package com.example.catalogofthings.di.binds

import com.example.catalogofthings.data.TagsRepository
import com.example.catalogofthings.data.TagsRepositoryImpl
import com.example.catalogofthings.data.db.NotesDatabase
import com.example.catalogofthings.data.db.TagsDAO
import com.example.catalogofthings.domain.tagsUseCases.CreateTagUseCase
import com.example.catalogofthings.domain.tagsUseCases.CreateTagUseCaseImpl
import com.example.catalogofthings.domain.tagsUseCases.DeleteTagUseCase
import com.example.catalogofthings.domain.tagsUseCases.DeleteTagUseCaseImpl
import com.example.catalogofthings.domain.tagsUseCases.GetTagUseCase
import com.example.catalogofthings.domain.tagsUseCases.GetTagUseCaseImpl
import com.example.catalogofthings.domain.tagsUseCases.GetTagsUseCase
import com.example.catalogofthings.domain.tagsUseCases.GetTagsUseCaseImpl
import com.example.catalogofthings.domain.tagsUseCases.UpdateTagUseCase
import com.example.catalogofthings.domain.tagsUseCases.UpdateTagUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface TagsBindsModule {

    @Binds
    @Singleton
    fun bindTagsRepository(impl: TagsRepositoryImpl): TagsRepository

    @Binds
    @Singleton
    fun bindCreateTagUseCase(impl: CreateTagUseCaseImpl): CreateTagUseCase

    @Binds
    @Singleton
    fun bindGetTagsUseCase(impl: GetTagsUseCaseImpl): GetTagsUseCase

    @Binds
    @Singleton
    fun bindGetTagUseCase(impl: GetTagUseCaseImpl): GetTagUseCase

    @Binds
    @Singleton
    fun bindUpdateTagUseCase(impl: UpdateTagUseCaseImpl): UpdateTagUseCase

    @Binds
    @Singleton
    fun bindDeleteTagUseCase(impl: DeleteTagUseCaseImpl): DeleteTagUseCase

    companion object {

        @Provides
        @Singleton
        fun provideTagsDAO (
            db: NotesDatabase
        ): TagsDAO =
            db.tagsDAO
    }
}