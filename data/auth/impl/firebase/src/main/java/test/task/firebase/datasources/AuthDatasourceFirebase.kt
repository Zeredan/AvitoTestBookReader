package test.task.firebase.datasources

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthDatasourceFirebase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    override val authState: Flow<AuthState> =
        callbackFlow {
            // Сразу отправляем Loading при старте
            trySend(AuthState.Loading)

            val listener = FirebaseAuth.AuthStateListener { auth ->
                val user = auth.currentUser

                val newState =
                    if (user == null) {
                        AuthState.NeedLogin
                    } else {
                        AuthState.Success(
                            AuthUser(uid = user.uid, email = user.email)
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