package com.example.catalogofthings.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.catalogofthings.data.ImagesRepository
import com.example.catalogofthings.data.ImagesRepositoryImpl
import com.example.catalogofthings.data.NotesRepository
import com.example.catalogofthings.data.NotesRepositoryImpl
import com.example.catalogofthings.data.TagsRepository
import com.example.catalogofthings.data.TagsRepositoryImpl
import com.example.catalogofthings.data.db.NotesDAO
import com.example.catalogofthings.data.db.NotesDatabase
import com.example.catalogofthings.domain.imagesUseCases.CreateImageUseCase
import com.example.catalogofthings.domain.imagesUseCases.CreateImageUseCaseImpl
import com.example.catalogofthings.domain.imagesUseCases.DeleteImageUseCase
import com.example.catalogofthings.domain.imagesUseCases.DeleteImageUseCaseImpl
import com.example.catalogofthings.domain.imagesUseCases.GetImageUseCase
import com.example.catalogofthings.domain.imagesUseCases.GetImageUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.AddTagToNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.AddTagToNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.CreateNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.CreateNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.DeleteNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.DeleteNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.DeleteTagFromNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.DeleteTagFromNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.GetAllFoldersUseCase
import com.example.catalogofthings.domain.notesUseCases.GetAllFoldersUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.GetFullNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.GetFullNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.GetNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.GetNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.GetNotesByFiltersUseCase
import com.example.catalogofthings.domain.notesUseCases.GetNotesByFiltersUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.GetNotesUseCase
import com.example.catalogofthings.domain.notesUseCases.GetNotesUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.UpdateNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.UpdateNoteUseCaseImpl
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
interface AppBindsModule {

    //Notes
    @Binds
    @Singleton
    fun bindNotesRepository(impl: NotesRepositoryImpl): NotesRepository

    @Binds
    @Singleton
    fun bindCreateNoteUseCase(impl: CreateNoteUseCaseImpl): CreateNoteUseCase

    @Binds
    @Singleton
    fun bindGetNotesUseCase(impl: GetNotesUseCaseImpl): GetNotesUseCase

    @Binds
    @Singleton
    fun bindGetNotesByFiltersUseCase(impl: GetNotesByFiltersUseCaseImpl): GetNotesByFiltersUseCase
    @Binds
    @Singleton
    fun bindGetNoteUseCase(impl: GetNoteUseCaseImpl): GetNoteUseCase

    @Binds
    @Singleton
    fun bindGetAllFoldersUseCase(impl: GetAllFoldersUseCaseImpl): GetAllFoldersUseCase

    @Binds
    @Singleton
    fun bindGetFullNoteUseCase(impl: GetFullNoteUseCaseImpl): GetFullNoteUseCase

    @Binds
    @Singleton
    fun bindUpdateNoteUseCase(impl: UpdateNoteUseCaseImpl): UpdateNoteUseCase

    @Binds
    @Singleton
    fun bindDeleteNoteUseCase(impl: DeleteNoteUseCaseImpl): DeleteNoteUseCase

    //Tags
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

    //Images
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

    //NoteTags
    @Binds
    @Singleton
    fun bindAddTagToNoteUseCase(impl: AddTagToNoteUseCaseImpl): AddTagToNoteUseCase

    @Binds
    @Singleton
    fun bindDeleteTagFromNoteUseCase(impl: DeleteTagFromNoteUseCaseImpl): DeleteTagFromNoteUseCase

    companion object {
        @Provides
        fun provideContext(app: Application): Context =
            app.applicationContext

        @Provides
        @Singleton
        fun provideDb(context: Context): NotesDatabase =
            Room.databaseBuilder(
                context,
                NotesDatabase::class.java,
                "notes.db"
            ).build()

        @Provides
        @Singleton
        fun provideDAO (
            db: NotesDatabase
        ): NotesDAO =
            db.notesDAO
    }
}