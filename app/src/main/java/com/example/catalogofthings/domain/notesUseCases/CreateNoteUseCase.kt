package com.example.catalogofthings.domain.notesUseCases

import com.example.catalogofthings.data.NotesRepository
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteWithTags
import javax.inject.Inject

interface CreateNoteUseCase {
    suspend operator fun invoke(noteEntity: NoteEntity): Int
}
class CreateNoteUseCaseImpl @Inject constructor(
    private val repository: NotesRepository
): CreateNoteUseCase {
    override suspend fun invoke(noteEntity: NoteEntity): Int =
        repository.createNote(noteEntity)

}