package com.example.catalogofthings.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteTagCrossRef
import com.example.catalogofthings.data.model.TagEntity

@Database(
    entities = [
        NoteEntity::class,
        TagEntity::class,
        NoteTagCrossRef::class
    ],
    version = 1
)
abstract class NotesDatabase: RoomDatabase() {
    abstract val notesDAO: NotesDAO
}