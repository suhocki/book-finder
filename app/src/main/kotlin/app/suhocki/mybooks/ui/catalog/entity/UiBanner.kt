package app.suhocki.mybooks.ui.catalog.entity

import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.ui.base.entity.UiItem

data class UiBanner(
    override val id: String,
    override val imageUrl: String,
    override val description: String,

    override var isNextPageTrigger: Boolean = false
) : Banner, UiItem