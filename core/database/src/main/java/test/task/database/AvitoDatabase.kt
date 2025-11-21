package test.task.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import test.task.database.dao.BooksDAO
import test.task.database.entities.BookEntity

@Database(entities = [BookEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AvitoDatabase : RoomDatabase() {
    abstract fun booksDao(): BooksDAO
}