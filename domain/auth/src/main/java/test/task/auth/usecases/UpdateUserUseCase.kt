package test.task.auth.usecases

import android.net.Uri
import test.task.auth.repositories.AuthRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, photoUri: String?, phone: String) = authRepository.updateUserProfile(name, photoUri, phone)
}