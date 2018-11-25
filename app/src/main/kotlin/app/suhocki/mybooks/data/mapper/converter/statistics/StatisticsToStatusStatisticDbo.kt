package app.suhocki.mybooks.data.mapper.converter.statistics

import app.suhocki.mybooks.data.mapper.GenericConverter
import app.suhocki.mybooks.data.room.entity.statistics.StatusStatisticsDbo
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.statistics.Statistics
import app.suhocki.mybooks.domain.model.statistics.StatusStatistics
import javax.inject.Inject


class StatisticsToStatusStatisticDbo @Inject constructor() :
    GenericConverter<LinkedHashMap<Category, Statistics>, ArrayList<StatusStatistics>>(
        LinkedHashMap<Category, Statistics>().javaClass,
        ArrayList<StatusStatistics>().javaClass
    ) {

    override fun convert(value: LinkedHashMap<Category, Statistics>): ArrayList<StatusStatistics> =
        value.entries.flatMap { (category, statistics) ->
            statistics.statuses.entries.map { (status, bookCount) ->
                StatusStatisticsDbo(
                    category.id,
                    status,
                    bookCount
                )
            }
        }.toCollection(arrayListOf())

    override val genericType = StatusStatistics::class.java
}