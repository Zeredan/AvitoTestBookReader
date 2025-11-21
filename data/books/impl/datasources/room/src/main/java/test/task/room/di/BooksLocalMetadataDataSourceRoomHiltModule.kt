package test.task.room.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.task.datasources.BooksLocalDataSource
import test.task.datasources.BooksLocalMetadataDataSource
import test.task.room.datasources.BooksLocalMetadataDataSourceRoomImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BooksLocalMetadataDataSourceRoomHiltModule {
    @Binds
    @Singleton
    abstract fun bindBooksLocalMetadataDataSourceRoomImpl(impl: BooksLocalMetadataDataSourceRoomImpl) : BooksLocalMetadataDataSource
}