package test.task.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import test.task.auth.usecases.GetAuthStateUseCase
import test.task.auth.usecases.LoginOrSignUpUseCase
import test.task.auth.usecases.LogoutUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val loginOrSignUpUseCase: LoginOrSignUpUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            getAuthStateUseCase().collect { aS ->
                _authState.value = aS
            }
        }
    }

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _isInputsValid = MutableStateFlow(false)
    val isInputsValid = _isInputsValid.asStateFlow()

    fun logInOrSignUp() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            println("QQWE: email: ${email.value}, password: ${password.value}")
            try {
                loginOrSignUpUseCase(email.value, password.value)
                println("QQWE G")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Неизвестная ошибка")
                print("QQWE E: $e")
            }
        }
    }

    fun setEmail(email: String){
        val hasCyrillic = Regex("[А-Яа-яЁё]")
        if (hasCyrillic.containsMatchIn(email)) return
        _email.value = email
        validateInputs()
    }

    fun setPassword(password: String){
        _password.value = password
        validateInputs()
    }

    private fun isEmailValid(email: String) : Boolean {
        val regex = Regex("^[A-Za-z][A-Za-z0-9_.+-]*@[A-Za-z][A-Za-z0-9-]*\\.[A-Za-z][A-Za-z0-9-]*$")
        return regex.matches(email)
    }
    private fun isPasswordValid(password: String) : Boolean {
        return password.length >= 5
    }
    private fun validateInputs() {
        val emailValue = _email.value
        val passwordValue = _password.value

        _isInputsValid.value = (isEmailValid(emailValue) && isPasswordValid(passwordValue))
    }

    fun moveToVK(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com"))
        context.startActivity(intent)
    }

    fun moveToOdnokl(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ok.ru"))
        context.startActivity(intent)
    }
}