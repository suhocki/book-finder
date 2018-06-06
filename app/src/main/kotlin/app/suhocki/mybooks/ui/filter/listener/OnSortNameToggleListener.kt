package app.suhocki.mybooks.ui.filter.listener

import app.suhocki.mybooks.domain.model.filter.SortName

interface OnSortNameToggleListener {

    fun onSortNameToggle(filterName: SortName)
}
