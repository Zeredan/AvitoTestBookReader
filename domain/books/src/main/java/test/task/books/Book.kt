package test.task.books

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val remoteUrl: String?,
    val localPath: String?,
    val isDownloaded: Boolean = false
)