package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.filter.FilterPrice
import app.suhocki.mybooks.domain.repository.StatisticsRepository
import javax.inject.Inject
import javax.inject.Provider

class FilterPriceEntityProvider @Inject constructor(
    private val category: Category,
    private val statisticsRepository: StatisticsRepository
) : Provider<FilterPrice> {

    override fun get() = object : FilterPrice {
        override val hintFrom by lazy {
            statisticsRepository.getPriceStatisticsFor(category).minPrice
        }

        override val hintTo by lazy {
            statisticsRepository.getPriceStatisticsFor(category).maxPrice
        }

        override var from: Double = 0.0

        override var to: Double = Int.MAX_VALUE.toDouble()
    }
}
