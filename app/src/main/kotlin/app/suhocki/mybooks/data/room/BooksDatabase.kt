package app.suhocki.mybooks.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import app.suhocki.mybooks.data.room.converter.ListToStringConverter
import app.suhocki.mybooks.data.room.dao.*
import app.suhocki.mybooks.data.room.entity.BannerEntity
import app.suhocki.mybooks.data.room.entity.BookEntity
import app.suhocki.mybooks.data.room.entity.CategoryEntity
import app.suhocki.mybooks.data.room.entity.ShopInfoEntity
import app.suhocki.mybooks.data.room.entity.statistics.*

@Database(
    entities = [
        BookEntity::class,
        CategoryEntity::class,
        AuthorStatisticsEntity::class,
        PublisherStatisticsEntity::class,
        YearStatisticsEntity::class,
        StatusStatisticsEntity::class,
        PriceStatisticsEntity::class,
        ShopInfoEntity::class,
        BannerEntity::class
    ],
    version = BooksDatabase.DATABASE_VERSION
)
@TypeConverters(ListToStringConverter::class)
abstract class BooksDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    abstract fun categoryDao(): CategoryDao

    abstract fun authorStatisticsDao(): AuthorStatisticsDao

    abstract fun publisherStatisticsDao(): PublisherStatisticsDao

    abstract fun statusStatisticsDao(): StatusStatisticsDao

    abstract fun yearStatisticsDao(): YearStatisticsDao

    abstract fun priceStatisticsDao(): PriceStatisticsDao

    abstract fun bannerDao(): BannerDao

    abstract fun shopInfoDao(): ShopInfoDao

    companion object {
        const val DATABASE_VERSION = 6
    }
}