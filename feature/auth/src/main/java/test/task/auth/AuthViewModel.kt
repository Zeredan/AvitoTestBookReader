package test.task.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import test.task.auth.usecases.GetAuthStateUseCase
import test.task.auth.usecases.LoginUseCase
import test.task.auth.usecases.LogoutUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val LoginUseCase: LoginUseCase,
    private val LogoutUseCase: LogoutUseCase
) : ViewModel() {
    val authState = getAuthStateUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, AuthState.Loading)
}