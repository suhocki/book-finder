package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.firestore.entity.FirestoreBanner
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.data.room.entity.BannerDbo
import javax.inject.Inject

class FirestoreBannerToBannerDbo @Inject constructor() :
    BaseConverter<FirestoreBanner, BannerDbo>(
        FirestoreBanner::class.java, BannerDbo::class.java
    ) {

    override fun convert(value: FirestoreBanner) =
        BannerDbo(
            id = value.id,
            imageUrl = value.imageUrl,
            description = value.description
        )
}