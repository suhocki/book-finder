package app.suhocki.mybooks.ui.filter.listener

import app.suhocki.mybooks.domain.model.filter.SortPrice

interface OnSortPriceToggleListener {

    fun onSortPriceToggle(sortPrice: SortPrice)
}
