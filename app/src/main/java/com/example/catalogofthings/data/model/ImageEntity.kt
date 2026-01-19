package com.example.catalogofthings.data.model

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.BLOB
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = ImageEntity.TABLE,
    foreignKeys = [ForeignKey(
        entity = NoteEntity::class,
        parentColumns = ["noteId"],
        childColumns = ["noteId"],
        onDelete = CASCADE
    )]
)
data class ImageEntity (
    @PrimaryKey(autoGenerate = true)
    val imageId: Int = 0,
    @ColumnInfo(typeAffinity = BLOB)
    val imageData: ByteArray?,
    var noteId: Int
) {
    companion object {
        const val TABLE = "images"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageEntity

        if (imageId != other.imageId) return false
        if (!imageData.contentEquals(other.imageData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageId
        result = 31 * result + imageData.contentHashCode()
        return result
    }
}