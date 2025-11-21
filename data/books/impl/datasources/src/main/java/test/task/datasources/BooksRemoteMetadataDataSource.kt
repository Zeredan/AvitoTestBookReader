package test.task.datasources

import kotlinx.coroutines.flow.Flow
import test.task.books.Book

interface BooksRemoteMetadataDataSource {
    suspend fun getBooks(uid: String): List<Book>
    suspend fun addBook(book: Book, uid: String): String
    suspend fun updateBook(bookId: String, book: Book)
    suspend fun deleteBook(bookId: String)
    suspend fun getBookById(bookId: String): Book?
    fun getBooksAsFlow(uid: String): Flow<List<Book>>
}