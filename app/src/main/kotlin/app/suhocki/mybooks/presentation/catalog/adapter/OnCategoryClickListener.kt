package app.suhocki.mybooks.presentation.catalog.adapter

import app.suhocki.mybooks.domain.model.Category

interface OnCategoryClickListener {
    fun onCategoryClick(category: Category)
}