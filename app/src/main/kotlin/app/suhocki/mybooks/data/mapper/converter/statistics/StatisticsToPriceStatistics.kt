package app.suhocki.mybooks.data.mapper.converter.statistics

import app.suhocki.mybooks.data.mapper.GenericConverter
import app.suhocki.mybooks.data.room.entity.PriceStatisticsEntity
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.statistics.PriceStatistics
import app.suhocki.mybooks.domain.model.statistics.Statistics
import javax.inject.Inject

class StatisticsToPriceStatistics @Inject constructor() :
    GenericConverter<LinkedHashMap<Category, Statistics>, ArrayList<PriceStatistics>>(
        LinkedHashMap<Category, Statistics>().javaClass,
        ArrayList<PriceStatistics>().javaClass
    ) {
    override val genericType = PriceStatistics::class.java

    override fun convert(value: LinkedHashMap<Category, Statistics>): ArrayList<PriceStatistics> =
        value.entries.asSequence().map { (category, statistics) ->
            val (minPrice, maxPrice) = statistics.prices
            PriceStatisticsEntity(category.id, minPrice, maxPrice)
        }.toCollection(arrayListOf())
}