package test.task.firebase.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.task.datasources.AuthDatasource
import test.task.firebase.datasources.AuthDatasourceFirebaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDatasourceFirebaseHilModule {
    @Binds
    @Singleton
    abstract fun bindAuthDatasourceFirebase(impl: AuthDatasourceFirebaseImpl): AuthDatasource
}