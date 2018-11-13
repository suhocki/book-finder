package app.suhocki.mybooks.data.room

import android.arch.persistence.db.SupportSQLiteQuery
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.data.room.dao.*
import app.suhocki.mybooks.data.room.entity.*
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.statistics.*
import app.suhocki.mybooks.domain.repository.BannersRepository
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.domain.repository.StatisticsRepository
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
    private val mapper: Mapper
) : BooksRepository, StatisticsRepository, BannersRepository {
    override fun getCategories(): List<Category> {
        return categoryDao.getAll()
    }

    override fun addCategories(categories: List<Category>) {
        categoryDao.insertAll(categories.map { mapper.map<CategoryEntity>(it) })
    }

    override fun getBooks(): List<BookEntity> =
        bookDao.getAll()

    override fun addBooks(books: List<Book>, uploadControl: UploadControlEntity?) {
        val data = books.map { mapper.map<BookEntity>(it) }
        bookDao.insertAll(data)
    }

    override fun getBooksFor(category: Category): List<Book> {
        return bookDao.getAllByCategory(category.id)
    }

    override fun search(text: String) =
        bookDao.find("%$text%")

    override fun filter(query: SupportSQLiteQuery): List<BookEntity> =
        bookDao.filter(query)

    override fun getAuthorStatisticsFor(category: Category): List<AuthorStatisticsEntity> {
        return authorStatisticsDao.getAllByCategory(category.id)
    }

    override fun getAuthorsWithName(
        searchQuery: String,
        category: Category
    ): List<AuthorStatisticsEntity> {
        return authorStatisticsDao.getAllByNameAndCategory("%$searchQuery%", category.id)
    }

    override fun getPublisherStatisticsFor(category: Category): List<PublisherStatisticsEntity> {
        return publisherStatisticsDao.getAllByCategory(category.id)
    }

    override fun getPublishersWithName(
        searchQuery: String,
        category: Category
    ): List<PublisherStatisticsEntity> {
        return publisherStatisticsDao.getAllByNameAndCategory("%$searchQuery%", category.id)
    }

    override fun getYearStatisticsFor(category: Category): List<YearStatistics> {
        return yearStatisticsDao.getAllByCategory(category.id)
    }

    override fun getStatusStatisticsFor(category: Category): List<StatusStatisticsEntity> {
        return statusStatisticsDao.getAllByCategory(category.id)
    }

    override fun getPriceStatisticsFor(category: Category): PriceStatisticsEntity {
        return priceStatisticsDao.getAllByCategory(category.id)
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
        bannerDao.insertAll(banners.map { BannerEntity(it.imageUrl, it.description) })
}