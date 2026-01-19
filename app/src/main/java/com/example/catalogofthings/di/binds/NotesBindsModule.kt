package com.example.catalogofthings.di.binds

import com.example.catalogofthings.data.NotesRepository
import com.example.catalogofthings.data.NotesRepositoryImpl
import com.example.catalogofthings.data.db.NotesDAO
import com.example.catalogofthings.data.db.NotesDatabase
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
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface NotesBindsModule {

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

    @Binds
    @Singleton
    fun bindAddTagToNoteUseCase(impl: AddTagToNoteUseCaseImpl): AddTagToNoteUseCase

    @Binds
    @Singleton
    fun bindDeleteTagFromNoteUseCase(impl: DeleteTagFromNoteUseCaseImpl): DeleteTagFromNoteUseCase

    companion object {

        @Provides
        @Singleton
        fun provideNotesDAO (
            db: NotesDatabase
        ): NotesDAO =
            db.notesDAO
    }
}