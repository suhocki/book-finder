package app.suhocki.mybooks.domain.model

import app.suhocki.mybooks.ui.catalog.adapter.CatalogItemType

interface CatalogItem {
    val type: CatalogItemType
}