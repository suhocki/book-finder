package app.suhocki.mybooks.data.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import app.suhocki.mybooks.data.database.entity.BookEntity

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<BookEntity>)

    @Query("SELECT * from Books")
    fun getAll(): List<BookEntity>

    @Query("SELECT * FROM Books WHERE category=:category")
    fun getAllByCategory(category: String): List<BookEntity>
}