package app.suhocki.mybooks.ui.filter.listener

import app.suhocki.mybooks.domain.model.filter.SortName

interface OnSortNameClickListener {

    fun onSortNameClick(filterName: SortName)
}
