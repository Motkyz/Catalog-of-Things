package com.example.catalogofthings.domain.notesUseCases

import com.example.catalogofthings.data.NotesRepository
import com.example.catalogofthings.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetAllFoldersUseCase {
    operator fun invoke(id : Int): Flow<List<NoteEntity>>
}

class GetAllFoldersUseCaseImpl @Inject constructor(
    private val repository: NotesRepository
): GetAllFoldersUseCase {
    override fun invoke(id : Int): Flow<List<NoteEntity>> =
        repository.getAllFolders(id)
}
