package test.task.books.usecases

import android.net.Uri
import test.task.books.Book
import test.task.books.repositories.BooksRepository
import javax.inject.Inject

class UpdateBookProgressUseCase @Inject constructor(
    private val repository: BooksRepository
) {
    suspend operator fun invoke(
        bookId: String,
        progress: Float,
    ) = repository.saveReadingProgress(bookId, progress)
}