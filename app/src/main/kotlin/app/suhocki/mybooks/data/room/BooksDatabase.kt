package app.suhocki.mybooks.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import app.suhocki.mybooks.data.room.converter.ListToStringConverter
import app.suhocki.mybooks.data.room.dao.*
import app.suhocki.mybooks.data.room.entity.BannerDbo
import app.suhocki.mybooks.data.room.entity.BookDbo
import app.suhocki.mybooks.data.room.entity.CategoryDbo
import app.suhocki.mybooks.data.room.entity.ShopInfoDbo
import app.suhocki.mybooks.data.room.entity.statistics.*

@Database(
    entities = [
        BookDbo::class,
        CategoryDbo::class,
        AuthorStatisticsDbo::class,
        PublisherStatisticsDbo::class,
        YearStatisticsDbo::class,
        StatusStatisticsDbo::class,
        PriceStatisticsDbo::class,
        ShopInfoDbo::class,
        BannerDbo::class
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

    object Table {
        const val BOOKS = "Books"
        const val CATEGORIES = "Categories"
        const val AUTHOR_STATISTICS = "AuthorStatistics"
        const val PUBLISHER_STATISTICS = "PublisherStatistics"
        const val YEAR_STATISTICS = "YearStatistics"
        const val STATUS_STATISTICS = "StatusStatistics"
        const val PRICE_STATISTICS = "PriceStatistics"
        const val SHOP_INFO = "ShopInfo"
        const val BANNERS = "Banners"
    }
}