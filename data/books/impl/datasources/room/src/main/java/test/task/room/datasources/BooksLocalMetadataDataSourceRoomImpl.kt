package test.task.room.datasources

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import test.task.books.Book
import test.task.database.dao.BooksDAO
import test.task.datasources.BooksLocalMetadataDataSource
import test.task.room.toBook
import test.task.room.toBookEntity
import javax.inject.Inject

class BooksLocalMetadataDataSourceRoomImpl @Inject constructor(
    private val dao: BooksDAO
) : BooksLocalMetadataDataSource {

    override suspend fun updateBooks(books: List<Book>) {
        dao.updateBooks(books.map { it.toBookEntity() })
    }

    override suspend fun addBook(book: Book) {
        dao.insert(book.toBookEntity())
    }

    override suspend fun getBooks(): List<Book> = dao.getAll().map { it.toBook() }

    override fun getBooksAsFlow(): Flow<List<Book>> = dao.getAllAsFlow().map { list ->
        list.map { it.toBook() }
    }

    override suspend fun getBookById(bookId: String): Book? = dao.getById(bookId)?.toBook()

    override suspend fun updateBook(book: Book) = dao.update(book.toBookEntity())

    override suspend fun deleteBook(bookId: String) = dao.delete(bookId)

    override suspend fun searchBooks(query: String): List<Book> = dao.search(query).map { it.toBook() }
}