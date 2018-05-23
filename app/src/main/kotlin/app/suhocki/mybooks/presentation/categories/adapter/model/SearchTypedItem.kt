package app.suhocki.mybooks.presentation.categories.adapter.model

import app.suhocki.mybooks.domain.model.TypedItem
import app.suhocki.mybooks.presentation.categories.adapter.ItemType

class SearchTypedItem(override val type: ItemType = ItemType.SEARCH) : TypedItem