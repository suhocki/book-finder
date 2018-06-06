package app.suhocki.mybooks.domain.model.statistics

import app.suhocki.mybooks.domain.model.filter.*

interface FilterItemStatistics : Statistics {
    val authorsFilterItems: MutableList<FilterAuthor>
    val publishersFilterItems: MutableList<FilterPublisher>
    val yearsFilterItems: MutableList<FilterYear>
    val statusesFilterItems: MutableList<FilterStatus>
    val pricesFilterItem: FilterPrice
}