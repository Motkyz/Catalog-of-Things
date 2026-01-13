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
import com.example.catalogofthings.domain.notesUseCases.AddImageToNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.AddImageToNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.AddTagToNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.AddTagToNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.CreateNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.CreateNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.DeleteImageFromNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.DeleteImageFromNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.DeleteTagFromNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.DeleteTagFromNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.GetFullNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.GetFullNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.GetNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.GetNoteUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.GetNotesUseCase
import com.example.catalogofthings.domain.notesUseCases.GetNotesUseCaseImpl
import com.example.catalogofthings.domain.notesUseCases.UpdateNoteUseCase
import com.example.catalogofthings.domain.notesUseCases.UpdateNoteUseCaseImpl
import com.example.catalogofthings.domain.tagsUseCases.CreateTagUseCase
import com.example.catalogofthings.domain.tagsUseCases.CreateTagUseCaseImpl
import com.example.catalogofthings.domain.tagsUseCases.GetTagsUseCase
import com.example.catalogofthings.domain.tagsUseCases.GetTagsUseCaseImpl
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
    fun bindGetNoteUseCase(impl: GetNoteUseCaseImpl): GetNoteUseCase

    @Binds
    @Singleton
    fun bindGetFullNoteUseCase(impl: GetFullNoteUseCaseImpl): GetFullNoteUseCase

    @Binds
    @Singleton
    fun bindUpdateNoteUseCase(impl: UpdateNoteUseCaseImpl): UpdateNoteUseCase

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

    //Images
    @Binds
    @Singleton
    fun bindImagesRepository(impl: ImagesRepositoryImpl): ImagesRepository

    @Binds
    @Singleton
    fun bindCreateImageUseCase(impl: CreateImageUseCaseImpl): CreateImageUseCase

    @Binds
    @Singleton
    fun bindDeleteImageUseCase(impl: DeleteImageUseCaseImpl): DeleteImageUseCase

    @Binds
    @Singleton
    fun bindGetImageUseCase(impl: GetImageUseCaseImpl): GetImageUseCase

    //NoteTags
    @Binds
    @Singleton
    fun bindAddTagToNoteUseCase(impl: AddTagToNoteUseCaseImpl): AddTagToNoteUseCase

    @Binds
    @Singleton
    fun bindDeleteTagFromNoteUseCase(impl: DeleteTagFromNoteUseCaseImpl): DeleteTagFromNoteUseCase

    //NoteImages
    @Binds
    @Singleton
    fun bindAddImageToNoteUseCase(impl: AddImageToNoteUseCaseImpl): AddImageToNoteUseCase

    @Binds
    @Singleton
    fun bindDeleteImageFromNoteUseCase(impl: DeleteImageFromNoteUseCaseImpl): DeleteImageFromNoteUseCase

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