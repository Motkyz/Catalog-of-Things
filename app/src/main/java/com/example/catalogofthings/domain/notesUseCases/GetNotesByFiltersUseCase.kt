package com.example.catalogofthings.domain.notesUseCases

import com.example.catalogofthings.data.NotesRepository
import com.example.catalogofthings.data.model.NoteWithTags
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetNotesByFiltersUseCase {
    operator fun invoke(search: String?, tagId: Int?): Flow<List<NoteWithTags>>
}

class GetNotesByFiltersUseCaseImpl @Inject constructor(
    private val repository: NotesRepository
): GetNotesByFiltersUseCase {
    override fun invoke(search: String?, tagId: Int?): Flow<List<NoteWithTags>> =
        repository.getNotesByFilters(search, tagId)
}