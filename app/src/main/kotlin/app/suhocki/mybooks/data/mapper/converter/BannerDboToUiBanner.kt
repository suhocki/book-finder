package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.data.room.entity.BannerDbo
import app.suhocki.mybooks.ui.catalog.entity.UiBanner
import javax.inject.Inject

class BannerDboToUiBanner @Inject constructor() :
    BaseConverter<BannerDbo, UiBanner>(
        BannerDbo::class.java, UiBanner::class.java
    ) {

    override fun convert(value: BannerDbo) =
        UiBanner(
            id = value.id,
            imageUrl = value.imageUrl,
            description = value.description
        )
}