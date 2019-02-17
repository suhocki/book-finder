package app.suhocki.mybooks.ui.catalog.entity

import app.suhocki.mybooks.domain.model.Banner

data class UiBanner(
    override val id: String,
    override val imageUrl: String,
    override val description: String
) : Banner