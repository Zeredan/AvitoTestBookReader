package test.task.auth.usecases

import test.task.auth.repositories.AuthRepository
import javax.inject.Inject

class LoginOrSignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        try {
            authRepository.login(email, password)
            return
        } catch (e: Exception) {
        }

        try {
            authRepository.signUp(email, password)
        } catch (e: Exception) {
            throw Exception("error logging in or signing up")
        }
    }
}