package app.suhocki.mybooks.domain.repository

import app.suhocki.mybooks.domain.model.Category

interface CategoriesRepository {
    fun getCategories(): List<Category>

    fun addCategories(categories: List<Category>)

    fun getCategoryById(id: String): Category =
        throw NotImplementedError()
}