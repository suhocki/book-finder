package app.suhocki.mybooks.data.database.dao

import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.*
import app.suhocki.mybooks.data.database.entity.BookEntity

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<BookEntity>)

    @Query("SELECT * FROM Books")
    fun getAll(): List<BookEntity>

    @Query("SELECT * FROM Books WHERE category=:category")
    fun getAllByCategory(category: String): List<BookEntity>

    @Query("SELECT * FROM Books WHERE productCode LIKE :q OR shortName LIKE :q OR fullName LIKE :q OR author LIKE :q OR publisher LIKE :q OR year LIKE :q LIMIT 20")
    fun find(q: String): List<BookEntity>

    @RawQuery(observedEntities = [BookEntity::class])
    fun filter(query: SupportSQLiteQuery): List<BookEntity>
}