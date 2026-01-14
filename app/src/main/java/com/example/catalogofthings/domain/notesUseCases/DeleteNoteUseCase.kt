package com.example.catalogofthings.domain.notesUseCases

import com.example.catalogofthings.data.NotesRepository
import com.example.catalogofthings.data.model.NoteEntity
import javax.inject.Inject

interface DeleteNoteUseCase {
    suspend operator fun invoke(noteEntity: NoteEntity): Int
}

class DeleteNoteUseCaseImpl @Inject constructor(
    private val repository: NotesRepository
): DeleteNoteUseCase {
    override suspend fun invoke(noteEntity: NoteEntity): Int =
        repository.deleteNote(noteEntity)
}