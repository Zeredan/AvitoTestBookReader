package test.task.impl.repositories

import kotlinx.coroutines.flow.Flow
import test.task.auth.AuthState
import test.task.auth.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(

) : AuthRepository {
    override suspend fun login(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override fun getAuthStateAsFlow(): Flow<AuthState> {
        TODO("Not yet implemented")
    }
}