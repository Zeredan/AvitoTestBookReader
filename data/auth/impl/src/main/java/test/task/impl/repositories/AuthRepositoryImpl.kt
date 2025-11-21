package test.task.impl.repositories

import kotlinx.coroutines.flow.Flow
import test.task.auth.AuthState
import test.task.auth.repositories.AuthRepository
import test.task.firebase.datasources.AuthDatasourceFirebase
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDatasourceFirebase: AuthDatasourceFirebase
) : AuthRepository {
    override suspend fun login(email: String, password: String) {
        authDatasourceFirebase.login(email, password)
    }

    override suspend fun signUp(email: String, password: String) {
        authDatasourceFirebase.signup(email, password)
    }

    override suspend fun logout() {
        authDatasourceFirebase.logout()
    }

    override fun getAuthStateAsFlow(): Flow<AuthState> {
        return authDatasourceFirebase.getAuthStateAsFlow()
    }
}