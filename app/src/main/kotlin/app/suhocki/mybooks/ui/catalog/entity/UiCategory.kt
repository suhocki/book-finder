package app.suhocki.mybooks.ui.catalog.entity

import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.ui.base.entity.UiItem

data class UiCategory(
    override val id: String,
    override val name: String,
    override val booksCount: Int,

    override var isNextPageTrigger: Boolean = false
) : Category, UiItem