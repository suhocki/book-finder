package app.suhocki.mybooks.data.mapper.converter.statistics

import app.suhocki.mybooks.data.mapper.GenericConverter
import app.suhocki.mybooks.data.room.entity.statistics.PriceStatisticsDbo
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.statistics.PriceStatistics
import app.suhocki.mybooks.domain.model.statistics.Statistics
import javax.inject.Inject

class StatisticsToPriceStatisticDbo @Inject constructor() :
    GenericConverter<LinkedHashMap<Category, Statistics>, ArrayList<PriceStatistics>>(
        LinkedHashMap<Category, Statistics>().javaClass,
        ArrayList<PriceStatistics>().javaClass
    ) {

    override fun convert(value: LinkedHashMap<Category, Statistics>): ArrayList<PriceStatistics> =
        value.entries.asSequence().map { (category, statistics) ->
            val (minPrice, maxPrice) = statistics.prices
            PriceStatisticsDbo(
                category.id,
                minPrice,
                maxPrice
            )
        }.toCollection(arrayListOf())

    override val genericType = PriceStatistics::class.java
}