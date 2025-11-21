package test.task.firebase_storage.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.task.datasources.BooksRemoteDataSource
import test.task.datasources.BooksRemoteMetadataDataSource
import test.task.firebase_storage.datasources.BooksRemoteDataSourceFirebaseStorageImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BooksRemoteDataSourceFirebaseStorageHiltModule {
    @Binds
    @Singleton
    abstract fun bindBooksRemoteDataSourceFireStorageImpl(impl: BooksRemoteDataSourceFirebaseStorageImpl): BooksRemoteDataSource
}