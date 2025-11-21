package test.task.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import test.task.database.entities.BookEntity

@Dao
interface BooksDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: BookEntity)

    @Query("SELECT * FROM books")
    suspend fun getAll(): List<BookEntity>

    @Query("SELECT * FROM books")
    fun getAllAsFlow(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): BookEntity?

    @Update
    suspend fun update(book: BookEntity)

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM books")
    suspend fun deleteAll()

    @Query("""
        SELECT * FROM books
        WHERE title LIKE '%' || :query || '%'
        OR author LIKE '%' || :query || '%'
    """)
    suspend fun search(query: String): List<BookEntity>

    @Transaction
    suspend fun updateBooks(books: List<BookEntity>) {
        deleteAll()
        insertBooks(books)
    }

}