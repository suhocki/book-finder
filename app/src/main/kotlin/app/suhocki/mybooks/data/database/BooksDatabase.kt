package app.suhocki.mybooks.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import app.suhocki.mybooks.data.database.dao.*
import app.suhocki.mybooks.data.database.entity.*

@Database(
    entities = [
        BookEntity::class,
        CategoryEntity::class,
        AuthorStatisticsEntity::class,
        PublisherStatisticsEntity::class,
        YearStatisticsEntity::class,
        StatusStatisticsEntity::class,
        PriceStatisticsEntity::class
    ],
    version = 1
)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun booksDao(): BookDao

    abstract fun categoryDao(): CategoryDao

    abstract fun authorStatisticsDao(): AuthorStatisticsDao

    abstract fun publisherStatisticsDao(): PublisherStatisticsDao

    abstract fun statusStatisticsDao(): StatusStatisticsDao

    abstract fun yearStatisticsDao(): YearStatisticsDao

    abstract fun priceStatisticsDao(): PriceStatisticsDao
}