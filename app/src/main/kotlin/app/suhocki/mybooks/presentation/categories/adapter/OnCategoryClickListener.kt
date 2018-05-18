package app.suhocki.mybooks.presentation.categories.adapter

import app.suhocki.mybooks.domain.model.Category

interface OnCategoryClickListener {
    fun onCategoryClick(category: Category)
}