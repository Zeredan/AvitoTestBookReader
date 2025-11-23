package test.task.datasources

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import test.task.auth.AuthState

interface AuthDatasource {
    suspend fun login(email: String, password: String)
    suspend fun signup(email: String, password: String)
    suspend fun logout()

    fun getAuthStateAsFlow(): Flow<AuthState>

    suspend fun updateUserProfile(
        name: String,
        photoUrl: String?,
        phoneNumber: String,
    ): Result<Unit>
}