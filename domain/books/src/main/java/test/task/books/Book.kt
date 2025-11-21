package test.task.books

data class Book(
    val id: String,
    val title: String,
    val author: String?,
    val remoteUrl: String?,
    val localPath: String?,
    val format: BookFormat,
    val coverUri: String?,
    val uploadedBy: String?,
    val updatedAt: Long
)

enum class BookFormat { TXT, EPUB, PDF }