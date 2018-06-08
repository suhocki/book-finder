package app.suhocki.mybooks.ui.filter.listener

import app.suhocki.mybooks.domain.model.filter.FilterPrice

interface OnFilterPriceChangeListener {

    fun onFilterPriceChange(type: FilterPrice.FilterPriceType)
}
