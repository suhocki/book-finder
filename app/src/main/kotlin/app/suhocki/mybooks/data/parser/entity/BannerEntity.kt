package app.suhocki.mybooks.data.parser.entity

import app.suhocki.mybooks.domain.model.Banner

class BannerEntity(
    override val imageUrl: String,
    override val description: String
) : Banner