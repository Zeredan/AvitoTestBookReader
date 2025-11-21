package test.task.firestore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.task.datasources.BooksRemoteMetadataDataSource
import test.task.firestore.datasources.BooksRemoteMetadataDataSourceFirestoreImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BooksRemoteMetadataDataSourceFirestoreHiltModule {
    @Binds
    @Singleton
    abstract fun bindBooksRemoteMetadataDataSourceFirestoreImpl(impl: BooksRemoteMetadataDataSourceFirestoreImpl): BooksRemoteMetadataDataSource
}