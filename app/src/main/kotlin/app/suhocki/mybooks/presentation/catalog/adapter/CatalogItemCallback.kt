package app.suhocki.mybooks.presentation.catalog.adapter

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.CatalogItem
import javax.inject.Inject

class CatalogItemCallback @Inject constructor() {

    private lateinit var value: DiffUtil.ItemCallback<CatalogItem>

    fun get(): DiffUtil.ItemCallback<CatalogItem> {
        return value
    }

    fun set(value: DiffUtil.ItemCallback<CatalogItem>) {
        this.value = value
    }
}