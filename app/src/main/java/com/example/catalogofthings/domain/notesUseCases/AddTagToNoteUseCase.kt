package com.example.catalogofthings.domain.notesUseCases

import com.example.catalogofthings.data.NotesRepository
import javax.inject.Inject

interface AddTagToNoteUseCase {
    suspend operator fun invoke(noteId: Int, tagId: Int)
}

class AddTagToNoteUseCaseImpl @Inject constructor(
    private val repository: NotesRepository
): AddTagToNoteUseCase {
    override suspend fun invoke(
        noteId: Int,
        tagId: Int
    ) {
        repository.addTag(noteId, tagId)
    }
}