package test.task.books.usecases

import kotlinx.coroutines.flow.Flow
import test.task.books.Book
import test.task.books.repositories.BooksRepository
import javax.inject.Inject

class GetBooksAsFlowUseCase @Inject constructor(
    private val repository: BooksRepository
) {
    operator fun invoke(): Flow<List<Book>> = repository.getBooksAsFlow()
}
