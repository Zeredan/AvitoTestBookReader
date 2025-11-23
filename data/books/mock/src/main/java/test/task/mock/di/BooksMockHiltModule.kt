package test.task.mock.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.task.books.repositories.BooksRepository
import test.task.mock.repositories.BooksRepositoryMock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BooksMockHiltModule {
    @Binds
    @Singleton
    abstract fun bindBooksRepository(impl: BooksRepositoryMock): BooksRepository
}