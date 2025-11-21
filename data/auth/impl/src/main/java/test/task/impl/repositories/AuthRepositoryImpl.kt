package test.task.impl.repositories

import kotlinx.coroutines.flow.Flow
import test.task.auth.AuthState
import test.task.auth.repositories.AuthRepository
import test.task.datasources.AuthDatasource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDatasource: AuthDatasource
) : AuthRepository {
    override suspend fun login(email: String, password: String) {
        authDatasource.login(email, password)
    }

    override suspend fun signUp(email: String, password: String) {
        authDatasource.signup(email, password)
    }

    override suspend fun logout() {
        authDatasource.logout()
    }

    override fun getAuthStateAsFlow(): Flow<AuthState> {
        return authDatasource.getAuthStateAsFlow()
    }
}