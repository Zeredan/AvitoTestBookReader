package test.task.uploader

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import test.task.books.usecases.UploadBookUseCase
import javax.inject.Inject

@HiltViewModel
class UploaderViewModel @Inject constructor(
    private val uploadBookUseCase: UploadBookUseCase
) : ViewModel(){
    private val _author = MutableStateFlow("")
    val author = _author.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _fileUri = MutableStateFlow<Uri?>(null)
    val fileUri = _fileUri.asStateFlow()

    fun setAuthor(value: String){
        _author.value = value
    }

    fun setTitle(value: String){
        _title.value = value
    }

    fun setFileUri(value: Uri?){
        _fileUri.value = value
    }


}