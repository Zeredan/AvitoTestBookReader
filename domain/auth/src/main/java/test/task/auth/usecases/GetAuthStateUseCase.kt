package test.task.auth.usecases

import test.task.auth.repositories.AuthRepository
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.getAuthStateAsFlow()
}