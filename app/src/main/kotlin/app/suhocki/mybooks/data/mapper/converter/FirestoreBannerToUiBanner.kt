package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.firestore.entity.FirestoreBanner
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.ui.catalog.entity.UiBanner
import javax.inject.Inject

class FirestoreBannerToUiBanner @Inject constructor() :
    BaseConverter<FirestoreBanner, UiBanner>(
        FirestoreBanner::class.java, UiBanner::class.java
    ) {

    override fun convert(value: FirestoreBanner) =
        UiBanner(
            id = value.id,
            imageUrl = value.imageUrl,
            description = value.description
        )
}