package app.suhocki.mybooks.domain.model

import app.suhocki.mybooks.presentation.categories.adapter.ItemType

interface TypedItem {
    val type: ItemType
}