package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.statistics.AuthorStatistics
import app.suhocki.mybooks.domain.model.statistics.PublisherStatistics
import app.suhocki.mybooks.domain.model.statistics.StatusStatistics
import app.suhocki.mybooks.domain.model.statistics.YearStatistics

interface StatisticDatabaseRepository {
    fun getAuthorStatisticsFor(category: Category): List<AuthorStatistics>

    fun getPublisherStatisticsFor(category: Category): List<PublisherStatistics>

    fun getYearStatisticsFor(category: Category): List<YearStatistics>

    fun getStatusStatisticsFor(category: Category): List<StatusStatistics>

    fun setAuthorStatistics(authorStatistics: List<AuthorStatistics>)

    fun setPublisherStatistics(publisherStatistics: List<PublisherStatistics>)

    fun setYearStatistics(yearStatistics: List<YearStatistics>)

    fun setStatusStatistics(statusStatistics: List<StatusStatistics>)
}