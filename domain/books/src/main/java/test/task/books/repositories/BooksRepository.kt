package test.task.books.repositories

import kotlinx.coroutines.flow.Flow
import test.task.books.Book
import test.task.books.DownloadState

interface BooksRepository {
    fun observeBooks(): Flow<List<Book>>
    suspend fun refreshRemoteBooks(): Result<Unit>

    fun downloadBook(bookId: String): Flow<DownloadState>
    suspend fun deleteLocalBook(bookId: String): Result<Unit>
}