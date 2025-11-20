package test.task.firebase.datasources

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import test.task.auth.AuthState
import test.task.auth.AuthUser
import javax.inject.Inject

class AuthDatasourceFirebase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun getAuthStateAsFlow(): Flow<AuthState> =
        callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                val user = auth.currentUser

                val newState =
                    if (user == null) {
                        AuthState.NeedAuth
                    } else {
                        AuthState.Success(
                            AuthUser(
                                uid = user.uid,
                                email = user.email,
                                displayName = user.displayName,
                                photoUrl = user.photoUrl?.toString()
                            )
                        )
                    }

                trySend(newState)
            }

            firebaseAuth.addAuthStateListener(listener)

            awaitClose {
                firebaseAuth.removeAuthStateListener(listener)
            }
        }

    suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signup(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun logout() {
        firebaseAuth.signOut()
    }
}