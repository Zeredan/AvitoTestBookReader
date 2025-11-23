package test.task.mock.repositories

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import test.task.books.Book
import test.task.books.repositories.BooksRepository
import test.task.books.repositories.DownloadProgress
import java.io.File
import javax.inject.Inject

class BooksRepositoryMock @Inject constructor(

) : BooksRepository {
    override fun getBooksAsFlow(): Flow<List<Book>> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadBook(title: String, author: String?, fileUri: Uri): Book {
        TODO("Not yet implemented")
    }

    override suspend fun downloadBook(book: Book): Flow<DownloadProgress> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLocalBook(bookId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRemoteBook(bookId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveReadingProgress(bookId: String, progress: Float) {
        TODO("Not yet implemented")
    }

    override suspend fun getReadingProgress(bookId: String): Float {
        TODO("Not yet implemented")
    }

    override suspend fun refreshBooks() {
        TODO("Not yet implemented")
    }

    override suspend fun getBookById(bookId: String): Book? {
        TODO("Not yet implemented")
    }

    override suspend fun searchBooks(query: String): List<Book> {
        TODO("Not yet implemented")
    }

    override suspend fun getBookFile(book: Book): File? {
        TODO("Not yet implemented")
    }

}
