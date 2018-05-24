package app.suhocki.mybooks.presentation.catalog.adapter

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.domain.model.CatalogItem
import javax.inject.Inject

class CatalogDiffer @Inject constructor() {

    private lateinit var value: AsyncListDiffer<CatalogItem>

    fun get(): AsyncListDiffer<CatalogItem> {
        return value
    }

    fun set(value: AsyncListDiffer<CatalogItem>) {
        this.value = value
    }
}