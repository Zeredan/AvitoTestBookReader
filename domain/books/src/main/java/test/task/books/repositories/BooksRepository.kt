package test.task.books.repositories

import kotlinx.coroutines.flow.Flow
import test.task.books.Book

interface BooksRepository {
    fun observeBooks(): Flow<List<Book>>
    suspend fun refreshRemoteBooks(): Result<Unit>
    fun downloadBook(bookId: String): kotlinx.coroutines.flow.Flow<DownloadState>
    suspend fun deleteLocalBook(bookId: String): Result<Unit>
}