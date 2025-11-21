package test.task.localfiles.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.task.datasources.BooksLocalDataSource
import test.task.localfiles.datasources.BooksLocalDataSourceFilesImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BooksLocalDataSourceFilesHiltModule {
    @Binds
    @Singleton
    abstract fun bindBooksLocalDataSourceFilesImpl(impl: BooksLocalDataSourceFilesImpl) : BooksLocalDataSource
}