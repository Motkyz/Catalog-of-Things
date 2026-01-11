package com.example.catalogofthings.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = NoteEntity.TABLE)
data class NoteEntity (
    @PrimaryKey(autoGenerate = true)
    val noteId: Int = 0,
    val title: String,
    val description: String,
    val date: Long,
    val parentId: Int = 0,
) {
    companion object {
        const val TABLE = "notes"
    }
}

data class NoteWithTags(
    @Embedded
    val note: NoteEntity,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "tagId",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val tags: List<TagEntity>
)