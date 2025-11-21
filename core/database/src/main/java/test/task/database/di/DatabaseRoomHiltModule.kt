package test.task.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import test.task.database.AvitoDatabase
import test.task.database.dao.BooksDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseRoomHiltModule {

    @Provides
    @Singleton
    fun provideAvitoDatabase(
        @ApplicationContext appContext: Context
    ) : AvitoDatabase {
        return Room.databaseBuilder(
            appContext,
            AvitoDatabase::class.java,
            "books_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBooksDAO(
        database: AvitoDatabase
    ) : BooksDAO {
        return database.booksDao()
    }
}