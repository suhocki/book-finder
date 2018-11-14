package app.suhocki.mybooks.data.mapper.converter.statistics

import app.suhocki.mybooks.data.mapper.GenericConverter
import app.suhocki.mybooks.data.room.entity.statistics.YearStatisticsEntity
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.statistics.Statistics
import app.suhocki.mybooks.domain.model.statistics.YearStatistics
import javax.inject.Inject

class StatisticsToYearStatistics @Inject constructor() :
    GenericConverter<LinkedHashMap<Category, Statistics>, ArrayList<YearStatistics>>(
        LinkedHashMap<Category, Statistics>().javaClass,
        ArrayList<YearStatistics>().javaClass
    ) {
    override val genericType = YearStatistics::class.java

    override fun convert(value: LinkedHashMap<Category, Statistics>): ArrayList<YearStatistics> =
        value.entries.flatMap { (category, statistics) ->
            statistics.years.entries.map { (year, bookCount) ->
                YearStatisticsEntity(
                    category.id,
                    year,
                    bookCount
                )
            }
        }.toCollection(arrayListOf())
}