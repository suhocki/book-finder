package app.suhocki.mybooks.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import app.suhocki.mybooks.data.database.dao.BookDao
import app.suhocki.mybooks.data.database.dao.CategoryDao
import app.suhocki.mybooks.data.database.entity.BookEntity
import app.suhocki.mybooks.data.database.entity.CategoryEntity

@Database(
    entities = [
        BookEntity::class,
        CategoryEntity::class
    ],
    version = 1
)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun booksDao(): BookDao

    abstract fun categoryDao(): CategoryDao
}