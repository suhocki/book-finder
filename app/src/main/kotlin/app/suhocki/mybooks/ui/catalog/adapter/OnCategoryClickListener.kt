package app.suhocki.mybooks.ui.catalog.adapter

import app.suhocki.mybooks.domain.model.Category

interface OnCategoryClickListener {
    fun onCategoryClick(category: Category)
}