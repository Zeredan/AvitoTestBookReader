package test.task.datasources

import kotlinx.coroutines.flow.Flow
import test.task.books.Book

interface BooksLocalMetadataDataSource {
    suspend fun updateBooks(books: List<Book>)
    suspend fun addBook(book: Book)
    suspend fun getBooks(): List<Book>
    fun getBooksAsFlow(): Flow<List<Book>>

    suspend fun getBookById(bookId: String): Book?
    suspend fun updateBook(book: Book)
    suspend fun deleteBook(bookId: String)
    suspend fun searchBooks(query: String): List<Book>
}