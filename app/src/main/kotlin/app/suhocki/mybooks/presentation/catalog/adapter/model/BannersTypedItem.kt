package app.suhocki.mybooks.presentation.catalog.adapter.model

import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.CatalogItem
import app.suhocki.mybooks.presentation.catalog.adapter.CatalogItemType

class BannersTypedItem(
    val banners: List<Banner>,
    override val type: CatalogItemType = CatalogItemType.BANNERS
) : CatalogItem