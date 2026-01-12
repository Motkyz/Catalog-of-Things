package com.example.catalogofthings.domain.notesUseCases

import com.example.catalogofthings.data.NotesRepository
import com.example.catalogofthings.data.model.NoteFull
import javax.inject.Inject

interface GetFullNoteUseCase {
    suspend operator fun invoke(id: Int): NoteFull?
}

class GetFullNoteUseCaseImpl @Inject constructor(
    private val repository: NotesRepository
): GetFullNoteUseCase {
    override suspend fun invoke(id: Int): NoteFull? =
        repository.getFullNote(id)

}