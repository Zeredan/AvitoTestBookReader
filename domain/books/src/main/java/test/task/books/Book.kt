package test.task.books

data class Book(
    val id: String,
    val title: String,
    val author: String?,
    val remoteUrl: String?,
    val localPath: String?,
    val format: BookFormat,
    val readProgress: Float
)

enum class BookFormat {
    TXT,
    EPUB,
    PDF;

    companion object {
        fun fromPath(path: String): BookFormat? {
            val extension = path.substringAfterLast('.', "")
            return fromExtension(extension)
        }

        fun fromExtension(extension: String): BookFormat? {
            return when (extension.lowercase()) {
                "txt" -> TXT
                "epub" -> EPUB
                "pdf" -> PDF
                else -> null
            }
        }

        fun fromMimeType(mimeType: String): BookFormat? {
            return when (mimeType) {
                "text/plain" -> TXT
                "application/epub+zip" -> EPUB
                "application/pdf" -> PDF
                else -> null
            }
        }
    }
}