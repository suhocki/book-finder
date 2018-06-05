package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.filter.*
import app.suhocki.mybooks.domain.model.statistics.FilterItemStatistics
import app.suhocki.mybooks.domain.repository.StatisticDatabaseRepository
import javax.inject.Inject
import javax.inject.Provider

class FilterItemStatisticsProvider @Inject constructor(
    private val category: Category,
    private val statisticsRepository: StatisticDatabaseRepository
) : Provider<FilterItemStatistics> {

    override fun get(): FilterItemStatistics = object : FilterItemStatistics {

        override val authorsFilterItems by lazy {
            mutableListOf<FilterAuthor>().apply {
                addAll(statisticsRepository.getAuthorStatisticsFor(category)
                    .map { (_, author, count) -> FilterAuthorEntity(author, count) })
            }
        }

        override val publishersFilterItems by lazy {
            mutableListOf<FilterPublisher>().apply {
                addAll(statisticsRepository.getPublisherStatisticsFor(category)
                    .map { (_, publisher, count) -> FilterPublisherEntity(publisher, count) })
            }
        }

        override val pricesFilterItem by lazy {
            statisticsRepository.getPriceStatisticsFor(category)
                .let { (_, minPrice, maxPrice) ->
                    FilterPriceEntity(minPrice, maxPrice)
                }
        }

        override val yearsFilterItems by lazy {
            mutableListOf<FilterYear>().apply {
                addAll(statisticsRepository.getYearStatisticsFor(category)
                    .map { (_, year, count) -> FilterYearEntity(year, count) })
            }
        }

        override val statusesFilterItems by lazy {
            mutableListOf<FilterStatus>().apply {
                addAll(statisticsRepository.getStatusStatisticsFor(category)
                    .map { (_, status, count) -> FilterStatusEntity(status, count) })
            }
        }

        override val authors by lazy {
            mutableMapOf<String, Int>().apply {
                statisticsRepository.getAuthorStatisticsFor(category)
                    .forEach { this[it.author] = it.count }
            }
        }

        override val publishers by lazy {
            mutableMapOf<String, Int>().apply {
                statisticsRepository.getPublisherStatisticsFor(category)
                    .forEach { this[it.publisher] = it.count }
            }
        }

        override val years by lazy {
            mutableMapOf<String, Int>().apply {
                statisticsRepository.getYearStatisticsFor(category)
                    .forEach { this[it.year] = it.count }
            }
        }

        override val statuses by lazy {
            mutableMapOf<String, Int>().apply {
                statisticsRepository.getStatusStatisticsFor(category)
                    .forEach { this[it.status] = it.count }
            }
        }

        override val prices by lazy {
            statisticsRepository.getPriceStatisticsFor(category)
                .let { (_, min, max) -> doubleArrayOf(min, max) }
        }
    }

    internal class FilterAuthorEntity(
        override val authorName: String,
        override val booksCount: Int,
        override var isChecked: Boolean = false
    ) : FilterAuthor

    internal class FilterPublisherEntity(
        override val publisherName: String,
        override val booksCount: Int,
        override var isChecked: Boolean = false
    ) : FilterPublisher

    internal class FilterYearEntity(
        override val year: String,
        override val booksCount: Int,
        override var isChecked: Boolean = false
    ) : FilterYear

    internal class FilterStatusEntity(
        override val status: String,
        override val booksCount: Int,
        override var isChecked: Boolean = false
    ) : FilterStatus

    internal class FilterPriceEntity(
        override val hintFrom: Double,
        override val hintTo: Double,
        override var from: Double = 0.0,
        override var to: Double = 0.0
    ) : FilterPrice
}