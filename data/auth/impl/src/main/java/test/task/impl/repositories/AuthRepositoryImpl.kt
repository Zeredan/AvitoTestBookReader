package test.task.impl.repositories

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import test.task.auth.AuthState
import test.task.auth.repositories.AuthRepository
import test.task.datasources.AuthDatasource
import test.task.datasources.BooksLocalDataSource
import test.task.datasources.BooksRemoteDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authDatasource: AuthDatasource,
    private val booksRemoteDataSource: BooksRemoteDataSource,
    private val booksLocalDataSource: BooksLocalDataSource
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
    ) {
        val auth = authDatasource.getAuthStateAsFlow().first()
        if (auth is AuthState.Success) {
            if (photoUrl != null) {
                try {
                    val inputState = context.contentResolver.openInputStream(Uri.parse(photoUrl)) ?: throw Exception("File not found")
                    val savedFile = booksLocalDataSource.saveFile(
                        inputState,
                        name
                    )
                    val result = booksRemoteDataSource.uploadFile(
                        savedFile.toUri(),
                        auth.user.uid,
                        name
                    )
                    authDatasource.updateUserProfile(name, result.url, phoneNumber)
                } catch (e: Exception) {
                    authDatasource.updateUserProfile(name, null, phoneNumber)
                }
            } else {
                authDatasource.updateUserProfile(name, null, phoneNumber)
            }
        }
    }
}