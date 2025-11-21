package test.task.effectivemobile.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import test.task.database.AvitoDatabase
import test.task.effectivemobile.database.dao.CoursesDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseRoomHiltModule {

    @Provides
    @Singleton
    fun provideCharactersDatabase(
        @ApplicationContext appContext: Context
    ) : AvitoDatabase {
        return Room.databaseBuilder(
            appContext,
            AvitoDatabase::class.java,
            "courses_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCoursesDAO(
        database: AvitoDatabase
    ) : CoursesDAO {
        return database.coursesDao
    }

    @Provides
    @Singleton
    fun provideFavoriteCoursesDAO(
        database: AvitoDatabase
    ) : FavoriteCoursesDAO {
        return database.favoriteCoursesDao
    }
}