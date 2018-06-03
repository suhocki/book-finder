package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.FilterCategory

interface FilterRepository {

    fun getFilterCategories(): List<FilterCategory>
}