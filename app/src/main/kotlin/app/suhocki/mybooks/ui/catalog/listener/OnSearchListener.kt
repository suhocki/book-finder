package app.suhocki.mybooks.ui.catalog.listener

import app.suhocki.mybooks.domain.model.Search

interface OnSearchListener {
    fun onSearch(search: Search)
}