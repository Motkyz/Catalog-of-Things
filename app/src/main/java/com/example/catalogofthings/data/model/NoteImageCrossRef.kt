package com.example.catalogofthings.data.model

import androidx.room.Entity

@Entity(
    tableName = NoteImageCrossRef.TABLE,
    primaryKeys = ["noteId", "imageId"]
)
data class NoteImageCrossRef (
    val noteId: Int,
    val imageId: Int
) {
    companion object {
        const val TABLE = "note_image_ref"
    }
}