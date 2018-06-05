package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.filter.FilterCategory
import app.suhocki.mybooks.domain.model.filter.SortName
import app.suhocki.mybooks.domain.model.filter.SortPrice

interface FilterRepository {

    fun getFilterCategories(): List<FilterCategory>

    fun getFilterByNameItems(): List<SortName>

    fun getFilterByPriceItems(): List<SortPrice>
}