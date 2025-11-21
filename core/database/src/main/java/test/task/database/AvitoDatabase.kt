package test.task.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import test.task.effectivemobile.database.Converters
import test.task.effectivemobile.database.dao.CoursesDAO
import test.task.effectivemobile.database.entities.CourseEntity

@Database(entities = [CourseEntity::class, FavoriteCourseEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class AvitoDatabase : RoomDatabase() {

}