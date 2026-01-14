package com.example.catalogofthings.domain.notesUseCases

import com.example.catalogofthings.data.NotesRepository
import javax.inject.Inject

interface DeleteTagFromNoteUseCase {
    suspend operator fun invoke(noteId: Int, tagId: Int)
}

class DeleteTagFromNoteUseCaseImpl @Inject constructor(
    private val repository: NotesRepository
): DeleteTagFromNoteUseCase {
    override suspend fun invoke(noteId: Int, tagId: Int) {
        repository.deleteTag(noteId, tagId)
    }
}