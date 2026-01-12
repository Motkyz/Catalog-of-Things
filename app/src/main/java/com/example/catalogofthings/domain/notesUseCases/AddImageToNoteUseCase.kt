package com.example.catalogofthings.domain.notesUseCases

import com.example.catalogofthings.data.NotesRepository
import javax.inject.Inject

interface AddImageToNoteUseCase {
    suspend operator fun invoke(noteId: Int, imageId: Int)
}

class AddImageToNoteUseCaseImpl @Inject constructor(
    private val repository: NotesRepository
): AddImageToNoteUseCase {
    override suspend fun invoke(noteId: Int, imageId: Int) {
        repository.addImage(noteId, imageId)
    }
}