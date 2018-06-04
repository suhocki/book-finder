package app.suhocki.mybooks.domain.model.statistics

import app.suhocki.mybooks.domain.model.filter.FilterAuthor
import app.suhocki.mybooks.domain.model.filter.FilterPublisher
import app.suhocki.mybooks.domain.model.filter.FilterStatus
import app.suhocki.mybooks.domain.model.filter.FilterYear

interface FilterItemStatistics : Statistics {
    val authorsFilterItems: List<FilterAuthor>
    val publishersFilterItems: List<FilterPublisher>
    val yearsFilterItems: List<FilterYear>
    val statusesFilterItems: List<FilterStatus>
}