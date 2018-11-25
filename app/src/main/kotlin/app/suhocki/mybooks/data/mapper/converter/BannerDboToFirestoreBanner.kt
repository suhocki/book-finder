package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.firestore.entity.FirestoreBanner
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.data.room.entity.BannerDbo
import javax.inject.Inject

class BannerDboToFirestoreBanner @Inject constructor() :
    BaseConverter<BannerDbo, FirestoreBanner>(
        BannerDbo::class.java, FirestoreBanner::class.java
    ) {

    override fun convert(value: BannerDbo) =
        FirestoreBanner(
            id = value.id,
            imageUrl = value.imageUrl,
            description = value.description
        )
}