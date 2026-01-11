package com.example.catalogofthings.domain.notesUseCases

import com.example.catalogofthings.data.NotesRepository
import com.example.catalogofthings.data.model.NoteEntity
import javax.inject.Inject

interface UpdateNoteUseCase {
    suspend operator fun invoke(oldNote: NoteEntity, newNote: NoteEntity)
}

class UpdateNoteUseCaseImpl @Inject constructor(
    private val repository: NotesRepository
): UpdateNoteUseCase {
    override suspend fun invoke(
        oldNote: NoteEntity,
        newNote: NoteEntity
    ) {
        repository.updateNote(oldNote, newNote)
    }
}