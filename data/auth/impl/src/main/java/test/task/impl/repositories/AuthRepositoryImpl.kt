package test.task.impl.repositories

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import test.task.auth.AuthState
import test.task.auth.repositories.AuthRepository
import test.task.datasources.AuthDatasource
import test.task.datasources.BooksRemoteDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDatasource: AuthDatasource,
    private val booksRemoteDataSource: BooksRemoteDataSource
) : AuthRepository {
    override fun getAuthStateAsFlow(): Flow<AuthState> {
        return authDatasource.getAuthStateAsFlow()
    }

    override suspend fun login(email: String, password: String) {
        authDatasource.login(email, password)
    }
    override suspend fun signUp(email: String, password: String) {
        authDatasource.signup(email, password)
    }
    override suspend fun logout() {
        authDatasource.logout()
    }

    override suspend fun updateUserProfile(
        name: String,
        photoUrl: String?,
        phoneNumber: String
    ): Result<Unit> {
        val auth = authDatasource.getAuthStateAsFlow().first()
        if (auth is AuthState.Success) {
            val result = booksRemoteDataSource.uploadFile(Uri.parse(photoUrl), auth.user.uid, photoUrl ?: "profile_photo")
            return if (result.isSuccess) {
                authDatasource.updateUserProfile(name, result.getOrNull()?.url, phoneNumber)
            } else {
                authDatasource.updateUserProfile(name, null, phoneNumber)
                Result.failure(Exception("cant upload photo"))
            }
        } else {
            return Result.failure(Exception("user not logged in"))
        }
    }
}