package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.di.CategoryId
import app.suhocki.mybooks.domain.model.filter.FilterPrice
import app.suhocki.mybooks.domain.repository.StatisticsRepository
import javax.inject.Inject
import javax.inject.Provider

class FilterPriceEntityProvider @Inject constructor(
    @CategoryId private val categoryId: String,
    private val statisticsRepository: StatisticsRepository
) : Provider<FilterPrice> {

    override fun get() = object : FilterPrice {
        override val hintFrom by lazy {
            statisticsRepository.getPriceStatisticsFor(categoryId).minPrice
        }

        override val hintTo by lazy {
            statisticsRepository.getPriceStatisticsFor(categoryId).maxPrice
        }

        override var from: Double = 0.0

        override var to: Double = Int.MAX_VALUE.toDouble()
    }
}
