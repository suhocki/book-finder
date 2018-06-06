package app.suhocki.mybooks.ui.filter.listener

import app.suhocki.mybooks.domain.model.filter.SortName

interface SortNameListener {

    fun onSortNameToggle(sortName: SortName)

    fun onSortNameClick(sortName: SortName)
}
