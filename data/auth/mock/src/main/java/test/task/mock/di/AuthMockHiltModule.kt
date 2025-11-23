package test.task.mock.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.task.auth.repositories.AuthRepository
import test.task.mock.repositories.AuthRepositoryMock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthMockHiltModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryMock): AuthRepository
}