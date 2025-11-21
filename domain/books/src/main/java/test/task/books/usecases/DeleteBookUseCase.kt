package test.task.books.usecases

import test.task.books.repositories.BooksRepository

class DeleteBookUseCase(
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