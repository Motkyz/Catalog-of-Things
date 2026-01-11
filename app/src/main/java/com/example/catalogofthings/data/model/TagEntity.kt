package com.example.catalogofthings.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = TagEntity.TABLE)
data class TagEntity (
    @PrimaryKey(autoGenerate = true)
    val tagId: Int = 0,
    val title: String,
    val description: String,
    val color: Int
) {
    companion object {
        const val TABLE = "tags"
    }
}

data class TagWithNotes(
    @Embedded
    val tag: TagEntity,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "noteId",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val notes: List<NoteEntity>
)