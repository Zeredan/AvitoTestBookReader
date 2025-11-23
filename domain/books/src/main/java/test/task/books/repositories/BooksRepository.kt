package test.task.books.repositories

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import test.task.books.Book
import java.io.File

interface BooksRepository {
    fun getBooksAsFlow(): Flow<List<Book>>
    suspend fun uploadBook(title: String, author: String?, fileUri: Uri): Book
    suspend fun downloadBook(book: Book): Flow<DownloadProgress>
    suspend fun deleteLocalBook(bookId: String)
    suspend fun deleteRemoteBook(bookId: String)
    suspend fun saveReadingProgress(bookId: String, progress: Float)

    suspend fun getReadingProgress(bookId: String): Float
    suspend fun refreshBooks()
    suspend fun getBookById(bookId: String): Book?
    suspend fun searchBooks(query: String): List<Book>
    suspend fun getBookFile(book: Book): File?
}

sealed class DownloadProgress {
    data class Progress(val percent: Float) : DownloadProgress()
    data class Success(val book: File) : DownloadProgress()
    data class Error(val exception: Exception) : DownloadProgress()
}
