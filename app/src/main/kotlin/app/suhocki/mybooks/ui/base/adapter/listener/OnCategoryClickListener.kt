package app.suhocki.mybooks.ui.base.adapter.listener

import app.suhocki.mybooks.domain.model.Category

interface OnCategoryClickListener {
    fun onCategoryClick(category: Category)
}