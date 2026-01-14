package com.example.catalogofthings.domain.notesUseCases

import com.example.catalogofthings.data.NotesRepository
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteWithTags
import javax.inject.Inject

interface GetNoteUseCase {
    suspend operator fun invoke(id: Int): NoteWithTags?
}

class GetNoteUseCaseImpl @Inject constructor(
    private val repository: NotesRepository
): GetNoteUseCase {
    override suspend fun invoke(id: Int): NoteWithTags? {
        return repository.getNote(id)
    }
}