package test.task.firebase.datasources

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import test.task.auth.AuthState
import test.task.auth.AuthUser
import test.task.datasources.AuthDatasource
import javax.inject.Inject

class AuthDatasourceFirebaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthDatasource{

    override fun getAuthStateAsFlow(): Flow<AuthState> =
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

    override suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signup(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}