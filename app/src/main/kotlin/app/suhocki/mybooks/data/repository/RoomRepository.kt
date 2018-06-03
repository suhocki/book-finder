package app.suhocki.mybooks.data.repository

import app.suhocki.mybooks.data.database.dao.*
import app.suhocki.mybooks.data.database.entity.*
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.statistics.AuthorStatistics
import app.suhocki.mybooks.domain.model.statistics.PublisherStatistics
import app.suhocki.mybooks.domain.model.statistics.StatusStatistics
import app.suhocki.mybooks.domain.model.statistics.YearStatistics
import app.suhocki.mybooks.domain.repository.BookDatabaseRepository
import app.suhocki.mybooks.domain.repository.StatisticDatabaseRepository
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val bookDao: BookDao,
    private val categoryDao: CategoryDao,
    private val publisherStatisticsDao: PublisherStatisticsDao,
    private val yearStatisticsDao: YearStatisticsDao,
    private val statusStatisticsDao: StatusStatisticsDao,
    private val authorStatisticsDao: AuthorStatisticsDao
) : BookDatabaseRepository, StatisticDatabaseRepository {
    override fun getCategories(): List<Category> {
        return categoryDao.getAll()
    }

    override fun saveCategories(categories: Set<Category>) =
        categoryDao.insertAll(categories.map { it as CategoryEntity }.distinct())

    override fun getBooks(): List<BookEntity> =
        bookDao.getAll()

    override fun saveBooks(books: List<Book>) =
        bookDao.insertAll(books.map { it as BookEntity })

    override fun getBooksFor(category: Category) =
        bookDao.getAllByCategory(category.name)

    override fun search(text: String) =
        bookDao.find("%$text%")

    override fun getAuthorStatisticsFor(category: Category) =
        authorStatisticsDao.getAllByCategory(category.name)

    override fun getPublisherStatisticsFor(category: Category) =
        publisherStatisticsDao.getAllByCategory(category.name)

    override fun getYearStatisticsFor(category: Category): List<YearStatistics> =
        yearStatisticsDao.getAllByCategory(category.name)

    override fun getStatusStatisticsFor(category: Category) =
        statusStatisticsDao.getAllByCategory(category.name)

    override fun setAuthorStatistics(authorStatistics: List<AuthorStatistics>) =
        authorStatisticsDao.insertAll(authorStatistics.map { it as AuthorStatisticsEntity })

    override fun setPublisherStatistics(publisherStatistics: List<PublisherStatistics>) =
        publisherStatisticsDao.insertAll(publisherStatistics.map { it as PublisherStatisticsEntity })

    override fun setYearStatistics(yearStatistics: List<YearStatistics>) =
        yearStatisticsDao.insertAll(yearStatistics.map { it as YearStatisticsEntity })

    override fun setStatusStatistics(statusStatistics: List<StatusStatistics>) =
        statusStatisticsDao.insertAll(statusStatistics.map { it as StatusStatisticsEntity })
}