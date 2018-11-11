package app.suhocki.mybooks.data.mapper.converter.statistics

import app.suhocki.mybooks.data.mapper.GenericConverter
import app.suhocki.mybooks.data.room.entity.CategoryEntity
import app.suhocki.mybooks.data.room.entity.PublisherStatisticsEntity
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.statistics.PublisherStatistics
import app.suhocki.mybooks.domain.model.statistics.Statistics
import javax.inject.Inject

class StatisticsToPublisherStatistics @Inject constructor() :
    GenericConverter<LinkedHashMap<Category, Statistics>, ArrayList<PublisherStatistics>>(
        LinkedHashMap<Category, Statistics>().javaClass,
        ArrayList<PublisherStatistics>().javaClass
    ) {
    override val genericType = PublisherStatistics::class.java

    override fun convert(value: LinkedHashMap<Category, Statistics>): ArrayList<PublisherStatistics> =
        value.entries.flatMap { (category, statistics) ->
            statistics.publishers.entries.map { (publisher, bookCount) ->
                PublisherStatisticsEntity((category as CategoryEntity).id, publisher, bookCount)
            }
        }.toCollection(arrayListOf())
}