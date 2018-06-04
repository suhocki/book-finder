package app.suhocki.mybooks.ui.base.listener.catalog

import app.suhocki.mybooks.domain.model.Category

interface OnCategoryClickListener {
    fun onCategoryClick(category: Category)
}