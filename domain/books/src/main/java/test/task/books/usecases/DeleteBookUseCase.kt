package test.task.books.usecases

import test.task.books.repositories.BooksRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(
    private val repository: BooksRepository
) {
    suspend operator fun invoke(bookId: String, remote: Boolean = false) {
        return if (remote) {
            repository.deleteRemoteBook(bookId)
        } else {
            repository.deleteLocalBook(bookId)
        }
    }
}