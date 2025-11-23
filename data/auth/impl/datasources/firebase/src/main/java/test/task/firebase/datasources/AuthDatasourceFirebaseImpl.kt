package test.task.firebase.datasources

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.tasks.await
import test.task.auth.AuthState
import test.task.auth.AuthUser
import test.task.datasources.AuthDatasource
import javax.inject.Inject

class AuthDatasourceFirebaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthDatasource{
    private val updates = MutableSharedFlow<Unit>()

    override fun getAuthStateAsFlow(): Flow<AuthState> {
        return merge(
            callbackFlow {
                val listener = FirebaseAuth.AuthStateListener { auth ->
                    trySend(Unit)
                }
                firebaseAuth.addAuthStateListener(listener)
                awaitClose {
                    firebaseAuth.removeAuthStateListener(listener)
                }
            },
            updates
        ).map {
            val user = firebaseAuth.currentUser
            val newState = if (user == null) {
                AuthState.NeedAuth
            } else {
                AuthState.Success(
                    AuthUser(
                        uid = user.uid,
                        email = user.email,
                        displayName = user.displayName,
                        photoUrl = user.photoUrl?.toString(),
                        phoneNumber = user.phoneNumber
                    )
                )
            }
            newState
        }
    }

    override suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signup(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun updateUserProfile(
        name: String,
        photoUrl: String?,
        phoneNumber: String
    ): Result<Unit> {
        println("UUUU: updating user:    $name ||| $photoUrl ||| $phoneNumber")
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("Not logged in"))

        val request = userProfileChangeRequest {
            displayName = name
            if (photoUrl != null) photoUri = Uri.parse(photoUrl)
        }

        return try {
            user.updateProfile(request).await()
            user.reload().await()
            updates.emit(Unit)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}