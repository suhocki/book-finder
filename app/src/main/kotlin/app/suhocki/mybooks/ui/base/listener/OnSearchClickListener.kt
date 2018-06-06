package app.suhocki.mybooks.ui.base.listener

import app.suhocki.mybooks.domain.model.Search

interface OnSearchClickListener {

    fun onExpandSearchClick()

    fun onCollapseSearchClick(): Boolean

    fun onClearSearchClick()

    fun onStartSearchClick(search: Search)
}