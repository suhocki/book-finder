package app.suhocki.mybooks.domain.model.statistics

import app.suhocki.mybooks.domain.model.filter.*

interface FilterItemStatistics : Statistics {
    val authorsFilterItems: List<FilterAuthor>
    val publishersFilterItems: List<FilterPublisher>
    val yearsFilterItems: List<FilterYear>
    val statusesFilterItems: List<FilterStatus>
    val pricesFilterItem: FilterPrice
}