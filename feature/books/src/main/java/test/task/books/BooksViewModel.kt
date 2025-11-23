package test.task.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import test.task.books.repositories.DownloadProgress
import test.task.books.usecases.DeleteBookUseCase
import test.task.books.usecases.DownloadBookUseCase
import test.task.books.usecases.GetBooksAsFlowUseCase
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val getBooksAsFlowUseCase: GetBooksAsFlowUseCase,
    private val downloadBookUseCase: DownloadBookUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
) : ViewModel(){
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    val books = combine(
        getBooksAsFlowUseCase(),
        _searchText
    ) { books, searchText ->
        books.filter { book ->
            book.title.contains(searchText, ignoreCase = true) || book.author?.contains(searchText, ignoreCase = true) == true
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _loadingProgress = MutableStateFlow<Float?>(null)
    val loadingProgress = _loadingProgress.asStateFlow()

    fun setSearchText(text: String) {
        _searchText.value = text
    }

    fun downloadBook(book: Book) {
        viewModelScope.launch {
            downloadBookUseCase(book).collect {
                if (it is DownloadProgress.Progress) {
                    _loadingProgress.value = it.percent
                } else if (it is DownloadProgress.Success) {
                    _loadingProgress.value = null
                }
            }
        }
    }

    fun deleteBook(book: Book, remote: Boolean = false) {
        viewModelScope.launch {
            deleteBookUseCase(book.id, remote)
        }
    }
}