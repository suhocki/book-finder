package app.suhocki.mybooks.data.mapper.converter.statistics

import app.suhocki.mybooks.data.mapper.GenericConverter
import app.suhocki.mybooks.data.room.entity.statistics.AuthorStatisticsDbo
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.statistics.AuthorStatistics
import app.suhocki.mybooks.domain.model.statistics.Statistics
import javax.inject.Inject

class StatisticsToAuthorStatisticDbo @Inject constructor() :
    GenericConverter<LinkedHashMap<Category, Statistics>, ArrayList<AuthorStatistics>>(
        LinkedHashMap<Category, Statistics>().javaClass,
        ArrayList<AuthorStatistics>().javaClass
    ) {

    override fun convert(value: LinkedHashMap<Category, Statistics>): ArrayList<AuthorStatistics> =
        value.entries.flatMap { (category, statistics) ->
            statistics.authors.entries.map { (authorName, bookCount) ->
                AuthorStatisticsDbo(
                    category.id,
                    authorName,
                    bookCount
                )
            }
        }.toCollection(arrayListOf())

    override val genericType = AuthorStatistics::class.java
}