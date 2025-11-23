package test.task.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import test.task.books.usecases.GetBooksAsFlowUseCase
import test.task.books.usecases.UpdateBookProgressUseCase
import test.task.settings.AvitoTheme
import test.task.settings.usecases.UCGetAppThemeAsFlow
import test.task.settings.usecases.UCGetFontSizeAsFlow
import test.task.settings.usecases.UCGetRowIntervalAsFlow
import test.task.settings.usecases.UCSetAppThemeAsFlow
import test.task.settings.usecases.UCSetFontSizeAsFlow
import test.task.settings.usecases.UCSetRowIntervalAsFlow
import java.io.File
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val getBooksAsFlowUseCase: GetBooksAsFlowUseCase,
    private val getAppThemeUseCase: UCGetAppThemeAsFlow,
    private val getFontSizeUseCase: UCGetFontSizeAsFlow,
    private val getRowIntervalUseCase: UCGetRowIntervalAsFlow,
    private val setAppThemeUseCase: UCSetAppThemeAsFlow,
    private val setFontSizeUseCase: UCSetFontSizeAsFlow,
    private val setRowIntervalUseCase: UCSetRowIntervalAsFlow,
    private val updateBookProgressUseCase: UpdateBookProgressUseCase
) : ViewModel() {

    val appTheme = getAppThemeUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, AvitoTheme.DARK)

    val fontSize = getFontSizeUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, 14f)

    val rowInterval = getRowIntervalUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, 18f)

    private val _selectedBookId = MutableStateFlow<String?>(null)
    val selectedBookId = _selectedBookId.asStateFlow()

    val selectedBook = _selectedBookId
        .filterNotNull()
        .flatMapLatest { id ->
            getBooksAsFlowUseCase().map { books ->
                books.firstOrNull { it.id == id }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(500), null)

    val chunks = selectedBook
        .map { it?.localPath }
        .distinctUntilChanged()
        .map {
            it?.let { path -> loadFileChunks(path) } ?: emptyList()
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private fun loadFileChunks(localPath: String) : List<String> {
        val file = File(localPath)
        if (!file.exists()) {
            return listOf("Файл не найден: $localPath")
        }

        val reader = file.bufferedReader()
        val buffer = CharArray(4096)
        val result = mutableListOf<String>()

        while (true) {
            val read = reader.read(buffer)
            if (read <= 0) break
            result.add(String(buffer, 0, read))
        }

        reader.close()
        return result
    }

    fun updateProgressUpper(progress: Float) = viewModelScope.launch {
        selectedBook.value?.let { book ->
            updateBookProgressUseCase(book.id, max(book.readProgress, progress))
        }
    }

    fun setAppTheme(theme: AvitoTheme) = viewModelScope.launch {
        setAppThemeUseCase(theme)
    }

    fun setFontSize(size: Float) = viewModelScope.launch {
        setFontSizeUseCase(size)
    }

    fun setRowInterval(interval: Float) = viewModelScope.launch {
        setRowIntervalUseCase(interval)
    }

    fun tryToInitialize(bookId: String) {
        _selectedBookId.value = bookId
    }
}
