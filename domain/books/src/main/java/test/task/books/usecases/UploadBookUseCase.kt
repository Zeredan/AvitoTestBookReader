package test.task.books.usecases

import android.net.Uri
import test.task.books.Book
import test.task.books.repositories.BooksRepository
import javax.inject.Inject

class UploadBookUseCase @Inject constructor(
    private val repository: BooksRepository
) {
    suspend operator fun invoke(
        title: String,
        author: String?,
        fileUri: Uri
    ): Book = repository.uploadBook(title, author, fileUri)
}