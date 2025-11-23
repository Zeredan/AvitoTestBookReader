package test.task.auth.repositories

import kotlinx.coroutines.flow.Flow
import test.task.auth.AuthState
import test.task.auth.AuthUser

interface AuthRepository {
    suspend fun login(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun logout()

    fun getAuthStateAsFlow(): Flow<AuthState>

    suspend fun updateUserProfile(
        name: String,
        photoUrl: String?,
        phoneNumber: String,
    ): Result<Unit>
}