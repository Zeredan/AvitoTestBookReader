package test.task.uploader

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import test.task.books.usecases.UploadBookUseCase
import javax.inject.Inject

@HiltViewModel
class UploaderViewModel @Inject constructor(
    private val uploadBookUseCase: UploadBookUseCase
) : ViewModel(){
    val successEventFlow = MutableSharedFlow<String>()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isValid = MutableStateFlow(false)
    val isValid = _isValid.asStateFlow()

    private val _author = MutableStateFlow("")
    val author = _author.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _fileUri = MutableStateFlow<Uri?>(null)
    val fileUri = _fileUri.asStateFlow()

    fun setAuthor(value: String) {
        val hasCyrillic = Regex("[А-Яа-яЁё]")
        if (hasCyrillic.containsMatchIn(value)) return
        _author.value = value
        checkValidity()
    }

    fun setTitle(value: String) {
        val hasCyrillic = Regex("[А-Яа-яЁё]")
        if (hasCyrillic.containsMatchIn(value)) return
        _title.value = value
        checkValidity()
    }

    fun setFileUri(value: Uri?) {
        _fileUri.value = value
        checkValidity()
    }

    fun checkValidity() {
        _isValid.value = _author.value.isNotEmpty() && _title.value.isNotEmpty() && _fileUri.value != null
    }

    fun uploadBook() {
        viewModelScope.launch {
            _errorMessage.value = null
            _isLoading.value = true
            try {
                uploadBookUseCase(
                    title = _title.value,
                    author = _author.value,
                    fileUri = _fileUri.value!!
                )
                successEventFlow.emit("Book uploaded successfully")
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
            _isLoading.value = false
        }
    }
}