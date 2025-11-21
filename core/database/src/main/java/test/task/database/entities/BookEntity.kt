package test.task.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String?,
    val remoteUrl: String?,
    val localPath: String?,
    val format: String,
    val coverUri: String?,
    val uploadedBy: String?,
    val updatedAt: Long
)

//fun BookEntity.toDomain() = Book(id, title, author, remoteUrl, localPath, BookFormat.valueOf(format), coverUri, uploadedBy, updatedAt)
//fun Book.toEntity() = BookEntity(id, title, author, remoteUrl, localPath, format.name, coverUri, uploadedBy, updatedAt)