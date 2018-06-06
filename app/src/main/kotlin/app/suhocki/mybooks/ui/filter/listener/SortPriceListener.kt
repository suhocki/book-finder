package app.suhocki.mybooks.ui.filter.listener

import app.suhocki.mybooks.domain.model.filter.SortPrice

interface SortPriceListener {

    fun onSortPriceToggle(sortPrice: SortPrice)

    fun onSortPriceClick(sortPrice: SortPrice)
}
