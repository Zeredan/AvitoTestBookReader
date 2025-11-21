package test.task.books.usecases

import kotlinx.coroutines.flow.Flow
import test.task.books.Book
import test.task.books.repositories.BooksRepository
import test.task.books.repositories.DownloadProgress
import javax.inject.Inject

class DownloadBookUseCase @Inject constructor(
    private val repository: BooksRepository
) {
    suspend operator fun invoke(book: Book): Flow<DownloadProgress> =
        repository.downloadBook(book)
}