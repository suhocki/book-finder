package app.suhocki.mybooks.ui.catalog.entity

import app.suhocki.mybooks.domain.model.Category

data class UiCategory(
    override val id: String,
    override val name: String,
    override val booksCount: Int
) : Category