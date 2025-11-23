package test.task.mock.repositories

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import test.task.auth.AuthState
import test.task.auth.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryMock @Inject constructor(
    @ApplicationContext private val context: Context,
) : AuthRepository {
    override suspend fun login(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override fun getAuthStateAsFlow(): Flow<AuthState> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserProfile(name: String, photoUrl: String?, phoneNumber: String) {
        TODO("Not yet implemented")
    }

}