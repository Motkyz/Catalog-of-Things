package com.example.catalogofthings.domain.notesUseCases

import com.example.catalogofthings.data.NotesRepository
import javax.inject.Inject

interface DeleteImageFromNoteUseCase {
    suspend operator fun invoke(noteId: Int, imageId: Int)
}

class DeleteImageFromNoteUseCaseImpl @Inject constructor(
    private val repository: NotesRepository
): DeleteImageFromNoteUseCase {
    override suspend fun invoke(noteId: Int, imageId: Int) {
        repository.deleteImage(noteId, imageId)
    }
}