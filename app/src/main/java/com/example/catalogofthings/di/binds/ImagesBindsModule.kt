package com.example.catalogofthings.di.binds

import com.example.catalogofthings.data.ImagesRepository
import com.example.catalogofthings.data.ImagesRepositoryImpl
import com.example.catalogofthings.data.db.ImagesDAO
import com.example.catalogofthings.data.db.NotesDatabase
import com.example.catalogofthings.domain.imagesUseCases.CreateImageUseCase
import com.example.catalogofthings.domain.imagesUseCases.CreateImageUseCaseImpl
import com.example.catalogofthings.domain.imagesUseCases.DeleteImageUseCase
import com.example.catalogofthings.domain.imagesUseCases.DeleteImageUseCaseImpl
import com.example.catalogofthings.domain.imagesUseCases.GetImageUseCase
import com.example.catalogofthings.domain.imagesUseCases.GetImageUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface ImagesBindsModule {

    @Binds
    @Singleton
    fun bindImagesRepository(impl: ImagesRepositoryImpl): ImagesRepository

    @Binds
    @Singleton
    fun bindCreateImageUseCase(impl: CreateImageUseCaseImpl): CreateImageUseCase

    @Binds
    @Singleton
    fun bindGetImageUseCase(impl: GetImageUseCaseImpl): GetImageUseCase

    @Binds
    @Singleton
    fun bindDeleteImageUseCase(impl: DeleteImageUseCaseImpl): DeleteImageUseCase

    companion object {

        @Provides
        @Singleton
        fun provideImagesDAO (
            db: NotesDatabase
        ): ImagesDAO =
            db.imagesDAO
    }
}