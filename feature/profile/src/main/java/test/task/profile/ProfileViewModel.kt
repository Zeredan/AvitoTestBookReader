package test.task.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import test.task.auth.AuthState
import test.task.auth.usecases.GetAuthStateUseCase
import test.task.auth.usecases.LoginOrSignUpUseCase
import test.task.auth.usecases.LogoutUseCase
import test.task.auth.usecases.UpdateUserUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _nickName = MutableStateFlow("")
    val nickName = _nickName.asStateFlow()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri = _imageUri.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing = _isEditing.asStateFlow()

    fun setNickName(name: String) {
        _nickName.value = name
    }

    fun setPhoneNumber(number: String) {
        _phoneNumber.value = number
    }

    fun startEditing() {
        _isEditing.value = true
    }

    fun stopEditing() {
        _isEditing.value = false
    }

    fun saveChanges() {
        if (nickName.value.isEmpty() && phoneNumber.value.isEmpty()) return
        viewModelScope.launch {
            _isLoading.value = true
            updateUserUseCase(nickName.value, _imageUri.value.toString(), phoneNumber.value)
            _isLoading.value = false
        }
    }

    fun setPhoto(uri: Uri) {
        _imageUri.value = uri
    }

    init {
        viewModelScope.launch {
            getAuthStateUseCase().collect { aS ->
                _authState.value = aS
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}