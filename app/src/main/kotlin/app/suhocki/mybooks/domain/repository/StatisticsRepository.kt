package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.statistics.*

interface StatisticsRepository {
    fun getAuthorStatisticsFor(categoryId: String): List<AuthorStatistics>

    fun getAuthorsWithName(searchQuery: String, categoryId: String): List<AuthorStatistics>

    fun getPublisherStatisticsFor(categoryId: String): List<PublisherStatistics>

    fun getPublishersWithName(searchQuery: String, categoryId: String): List<PublisherStatistics>

    fun getYearStatisticsFor(categoryId: String): List<YearStatistics>

    fun getStatusStatisticsFor(categoryId: String): List<StatusStatistics>

    fun getPriceStatisticsFor(categoryId: String): PriceStatistics

    fun setAuthorStatistics(authorStatistics: List<AuthorStatistics>)

    fun setPublisherStatistics(publisherStatistics: List<PublisherStatistics>)

    fun setYearStatistics(yearStatistics: List<YearStatistics>)

    fun setStatusStatistics(statusStatistics: List<StatusStatistics>)

    fun setPriceStatistics(priceStatistics: List<PriceStatistics>)
}