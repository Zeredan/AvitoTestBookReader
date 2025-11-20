package test.task.auth

sealed interface AuthState {
    data object Loading : AuthState
    data object NeedAuth : AuthState
    data class Success(val user: AuthUser) : AuthState
}