package test.task.books.usecases

import test.task.books.Book
import test.task.books.repositories.BooksRepository
import javax.inject.Inject

class SearchBooksUseCase @Inject constructor(
    private val repository: BooksRepository
) {
    suspend operator fun invoke(query: String): List<Book> =
        repository.searchBooks(query)
}
