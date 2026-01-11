package com.example.catalogofthings.data.model

import androidx.room.Entity

@Entity(tableName = NoteTagCrossRef.TABLE, primaryKeys = ["noteId", "tagId"])
data class NoteTagCrossRef (
    val noteId: Int,
    val tagId: Int
) {
    companion object {
        const val TABLE = "note_tag_ref"
    }
}