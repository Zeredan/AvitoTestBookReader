package test.task.books.repositories

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import test.task.books.Book
import test.task.books.DownloadState
import java.io.File

interface BooksRepository {
    fun getBooksStream(): Flow<List<Book>>
    suspend fun refreshBooks()

    suspend fun uploadBook(
        title: String,
        author: String,
        fileUri: Uri
    ): Result<Unit>

    suspend fun downloadBook(book: Book): File

    suspend fun deleteLocalBook(bookId: String)
    suspend fun searchBooks(query: String): List<Book>
}