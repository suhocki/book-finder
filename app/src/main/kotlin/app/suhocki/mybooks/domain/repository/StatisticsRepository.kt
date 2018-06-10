package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.statistics.*

interface StatisticsRepository {
    fun getAuthorStatisticsFor(category: Category): List<AuthorStatistics>

    fun getAuthorsWithName(searchQuery: String, category: Category): List<AuthorStatistics>

    fun getPublisherStatisticsFor(category: Category): List<PublisherStatistics>

    fun getPublishersWithName(searchQuery: String, category: Category): List<PublisherStatistics>

    fun getYearStatisticsFor(category: Category): List<YearStatistics>

    fun getStatusStatisticsFor(category: Category): List<StatusStatistics>

    fun getPriceStatisticsFor(category: Category): PriceStatistics

    fun setAuthorStatistics(authorStatistics: List<AuthorStatistics>)

    fun setPublisherStatistics(publisherStatistics: List<PublisherStatistics>)

    fun setYearStatistics(yearStatistics: List<YearStatistics>)

    fun setStatusStatistics(statusStatistics: List<StatusStatistics>)

    fun setPriceStatistics(priceStatistics: List<PriceStatistics>)
}