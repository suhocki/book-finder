package app.suhocki.mybooks.data.database

import android.arch.persistence.db.SupportSQLiteQuery
import app.suhocki.mybooks.data.database.dao.*
import app.suhocki.mybooks.data.database.entity.*
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.statistics.*
import app.suhocki.mybooks.domain.repository.BannersRepository
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.domain.repository.StatisticsRepository
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val bookDao: BookDao,
    private val categoryDao: CategoryDao,
    private val publisherStatisticsDao: PublisherStatisticsDao,
    private val yearStatisticsDao: YearStatisticsDao,
    private val statusStatisticsDao: StatusStatisticsDao,
    private val authorStatisticsDao: AuthorStatisticsDao,
    private val priceStatisticsDao: PriceStatisticsDao,
    private val bannerDao: BannerDao
) : BooksRepository, StatisticsRepository, BannersRepository {
    override fun getCategories(): List<Category> {
        return categoryDao.getAll()
    }

    override fun setCategories(categories: Set<Category>) =
        categoryDao.insertAll(categories.map { it as CategoryEntity }.distinct())

    override fun getBooks(): List<BookEntity> =
        bookDao.getAll()

    override fun setBooks(books: List<Book>) =
        bookDao.insertAll(books.map { it as BookEntity })

    override fun getBooksFor(category: Category) =
        bookDao.getAllByCategory(category.name)

    override fun search(text: String) =
        bookDao.find("%$text%")

    override fun filter(query: SupportSQLiteQuery): List<BookEntity> =
        bookDao.filter(query)

    override fun getAuthorStatisticsFor(category: Category) =
        authorStatisticsDao.getAllByCategory(category.name)

    override fun getAuthorsWithName(searchQuery: String, category: Category) =
        authorStatisticsDao.getAllByNameAndCategory("%$searchQuery%", category.name)

    override fun getPublisherStatisticsFor(category: Category) =
        publisherStatisticsDao.getAllByCategory(category.name)

    override fun getPublishersWithName(searchQuery: String, category: Category) =
        publisherStatisticsDao.getAllByNameAndCategory("%$searchQuery%", category.name)

    override fun getYearStatisticsFor(category: Category): List<YearStatistics> =
        yearStatisticsDao.getAllByCategory(category.name)

    override fun getStatusStatisticsFor(category: Category) =
        statusStatisticsDao.getAllByCategory(category.name)

    override fun getPriceStatisticsFor(category: Category) =
        priceStatisticsDao.getAllByCategory(category.name)

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
        bannerDao.insertAll(banners.map { BannerEntity(it.imageUrl, it.description) })
}