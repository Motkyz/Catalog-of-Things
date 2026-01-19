package com.example.catalogofthings.domain.notesUseCases

import com.example.catalogofthings.data.NotesRepository
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteWithTags
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetNotesUseCase {
    suspend operator fun invoke(id: Int?): Flow<List<NoteWithTags>>
}

class GetNotesUseCaseImpl @Inject constructor(
    private val repository: NotesRepository
): GetNotesUseCase {
    override suspend fun invoke(id: Int?): Flow<List<NoteWithTags>> {
        return repository.getNotes(id)
    }
}