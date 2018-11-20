package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.*
import app.suhocki.mybooks.data.room.entity.BookEntity

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<BookEntity>)

    @Query("SELECT * FROM Books")
    fun getAll(): List<BookEntity>

    @Query("SELECT * FROM Books WHERE id=:bookId")
    fun getBookById(bookId: String): BookEntity

    @Query("SELECT * FROM Books WHERE categoryId=:categoryId")
    fun getAllByCategory(categoryId: String): List<BookEntity>

    @Query("SELECT * FROM Books WHERE id LIKE :q OR shortName LIKE :q OR fullName LIKE :q OR author LIKE :q OR publisher LIKE :q OR year LIKE :q LIMIT 20")
    fun find(q: String): List<BookEntity>

    @RawQuery(observedEntities = [BookEntity::class])
    fun filter(query: SupportSQLiteQuery): List<BookEntity>
}