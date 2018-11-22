package app.suhocki.mybooks.data.room

import android.arch.persistence.db.SupportSQLiteQuery
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.data.room.dao.*
import app.suhocki.mybooks.data.room.entity.BannerEntity
import app.suhocki.mybooks.data.room.entity.BookEntity
import app.suhocki.mybooks.data.room.entity.CategoryEntity
import app.suhocki.mybooks.data.room.entity.ShopInfoEntity
import app.suhocki.mybooks.data.room.entity.statistics.*
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.ShopInfo
import app.suhocki.mybooks.domain.model.statistics.*
import app.suhocki.mybooks.domain.repository.*
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val bookDao: BookDao,
    private val categoryDao: CategoryDao,
    private val publisherStatisticsDao: PublisherStatisticsDao,
    private val yearStatisticsDao: YearStatisticsDao,
    private val statusStatisticsDao: StatusStatisticsDao,
    private val authorStatisticsDao: AuthorStatisticsDao,
    private val priceStatisticsDao: PriceStatisticsDao,
    private val bannerDao: BannerDao,
    private val infoDao: ShopInfoDao,
    private val mapper: Mapper
) : BooksRepository, CategoriesRepository, StatisticsRepository, BannersRepository, InfoRepository {
    override fun getCategories(): List<Category> {
        return categoryDao.getAll()
    }

    override fun addCategories(categories: List<Category>) {
        categoryDao.insertAll(categories.map { mapper.map<CategoryEntity>(it) })
    }

    override fun getCategoryById(id: String) =
        categoryDao.getCategoryById(id)

    override fun getBookById(id: String): Book {
        return bookDao.getBookById(id)
    }

    override fun getBooks(): List<BookEntity> =
        bookDao.getAll()

    override fun addBooks(books: List<Book>, uploadControl: UploadControlEntity?) {
        val data = books.map { mapper.map<BookEntity>(it) }
        bookDao.insertAll(data)
    }

    override fun getBooksFor(categoryId: String): List<Book> {
        return bookDao.getAllByCategory(categoryId)
    }

    override fun search(text: String) =
        bookDao.find("%$text%")

    override fun filter(query: SupportSQLiteQuery): List<BookEntity> =
        bookDao.filter(query)

    override fun getAuthorStatisticsFor(categoryId: String): List<AuthorStatisticsEntity> {
        return authorStatisticsDao.getAllByCategory(categoryId)
    }

    override fun getAuthorsWithName(
        searchQuery: String,
        categoryId: String
    ): List<AuthorStatisticsEntity> {
        return authorStatisticsDao.getAllByNameAndCategory("%$searchQuery%", categoryId)
    }

    override fun getPublisherStatisticsFor(categoryId: String): List<PublisherStatisticsEntity> {
        return publisherStatisticsDao.getAllByCategory(categoryId)
    }

    override fun getPublishersWithName(
        searchQuery: String,
        categoryId: String
    ): List<PublisherStatisticsEntity> {
        return publisherStatisticsDao.getAllByNameAndCategory("%$searchQuery%", categoryId)
    }

    override fun getYearStatisticsFor(categoryId: String): List<YearStatistics> {
        return yearStatisticsDao.getAllByCategory(categoryId)
    }

    override fun getStatusStatisticsFor(categoryId: String): List<StatusStatisticsEntity> {
        return statusStatisticsDao.getAllByCategory(categoryId)
    }

    override fun getPriceStatisticsFor(categoryId: String): PriceStatisticsEntity {
        return priceStatisticsDao.getAllByCategory(categoryId)
    }

    override fun setAuthorStatistics(authorStatistics: List<AuthorStatistics>) =
        authorStatisticsDao.insertAll(authorStatistics.map { it as AuthorStatisticsEntity })

    override fun setPublisherStatistics(publisherStatistics: List<PublisherStatistics>) =
        publisherStatisticsDao.insertAll(publisherStatistics.map { it as PublisherStatisticsEntity })

    override fun setYearStatistics(yearStatistics: List<YearStatistics>) =
        yearStatisticsDao.insertAll(yearStatistics.map { it as YearStatisticsEntity })

    override fun setStatusStatistics(statusStatistics: List<StatusStatistics>) =
        statusStatisticsDao.insertAll(statusStatistics.map { it as StatusStatisticsEntity })

    override fun setPriceStatistics(priceStatistics: List<PriceStatistics>) =
        priceStatisticsDao.insertAll(priceStatistics.map { it as PriceStatisticsEntity })

    override fun getBanners(): List<Banner> =
        bannerDao.getAll()

    override fun setBanners(banners: List<Banner>) =
        bannerDao.insertAll(banners.map { BannerEntity(it.id, it.imageUrl, it.description) })

    override fun getShopInfo() =
        infoDao.get()

    override fun setShopInfo(shopInfo: ShopInfo) =
        infoDao.insert(shopInfo as ShopInfoEntity)
}