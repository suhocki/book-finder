package app.suhocki.mybooks.domain.model

import app.suhocki.mybooks.presentation.catalog.adapter.CatalogItemType

interface CatalogItem {
    val type: CatalogItemType
}