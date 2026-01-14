package com.example.catalogofthings.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catalogofthings.data.model.ImageEntity
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.NoteImageCrossRef
import com.example.catalogofthings.data.model.NoteTagCrossRef
import com.example.catalogofthings.data.model.TagEntity

@Database(
    entities = [
        NoteEntity::class,
        TagEntity::class,
        NoteTagCrossRef::class,
        ImageEntity::class,
        NoteImageCrossRef::class
    ],
    version = 1
)
abstract class NotesDatabase: RoomDatabase() {
    abstract val notesDAO: NotesDAO
}