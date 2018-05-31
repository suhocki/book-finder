package app.suhocki.mybooks.ui.base.listener

import app.suhocki.mybooks.domain.model.Search

interface OnSearchListener {
    fun onSearch(search: Search)
}