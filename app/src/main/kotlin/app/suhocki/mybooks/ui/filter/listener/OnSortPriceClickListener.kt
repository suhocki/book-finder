package app.suhocki.mybooks.ui.filter.listener

import app.suhocki.mybooks.domain.model.filter.SortPrice

interface OnSortPriceClickListener {

    fun onSortPriceClick(sortPrice: SortPrice)
}
