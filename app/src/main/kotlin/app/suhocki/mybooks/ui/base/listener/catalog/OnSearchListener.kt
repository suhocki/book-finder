package app.suhocki.mybooks.ui.base.listener.catalog

import app.suhocki.mybooks.domain.model.Search

interface OnSearchListener {
    fun onSearch(search: Search)
}