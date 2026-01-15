package com.example.catalogofthings.data.model

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.BLOB
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
    val isFolder: Boolean = false,
    val description: String,
    val date: Long = System.currentTimeMillis(),

    val parentId: Int = 0,
    val childrenCount: Int = 0,

    @ColumnInfo(typeAffinity = BLOB)
    val icon: ByteArray? = null
) {
    companion object {
        const val TABLE = "notes"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteEntity

        if (noteId != other.noteId) return false
        if (isFolder != other.isFolder) return false
        if (date != other.date) return false
        if (parentId != other.parentId) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (!icon.contentEquals(other.icon)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = noteId
        result = 31 * result + isFolder.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + parentId
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + icon.contentHashCode()
        return result
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

data class NoteFull(
    @Embedded
    val note: NoteEntity,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "tagId",
        associateBy = Junction(NoteTagCrossRef::class)
    )
    val tags: List<TagEntity>,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "imageId",
        associateBy = Junction(NoteImageCrossRef::class)
    )
    val images: List<ImageEntity>
)