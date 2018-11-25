package app.suhocki.mybooks.data.room.dao

import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.*
import app.suhocki.mybooks.data.room.entity.BookDbo

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<BookDbo>)

    @Query("SELECT * FROM Books")
    fun getAll(): List<BookDbo>

    @Query("SELECT * FROM Books WHERE creationDate=:creationDate")
    fun getAll(creationDate: String): List<BookDbo>

    @Query("SELECT * FROM Books WHERE id=:bookId")
    fun getBookById(bookId: String): BookDbo

    @Query("SELECT * FROM Books WHERE categoryId=:categoryId")
    fun getAllByCategory(categoryId: String): List<BookDbo>

    @Query("SELECT * FROM Books WHERE id LIKE :q OR shortName LIKE :q OR fullName LIKE :q OR author LIKE :q OR publisher LIKE :q OR year LIKE :q LIMIT 20")
    fun find(q: String): List<BookDbo>

    @RawQuery(observedEntities = [BookDbo::class])
    fun filter(query: SupportSQLiteQuery): List<BookDbo>
}