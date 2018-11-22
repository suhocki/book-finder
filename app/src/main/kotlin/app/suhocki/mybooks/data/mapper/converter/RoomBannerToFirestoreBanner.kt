package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.mapper.BaseConverter
import javax.inject.Inject

class RoomBannerToFirestoreBanner @Inject constructor() :
    BaseConverter<RoomBannerEntity, FirestoreBannerEntity>(
        RoomBannerEntity::class.java, FirestoreBannerEntity::class.java
    ) {

    override fun convert(value: RoomBannerEntity) =
        FirestoreBannerEntity(
            id = value.id,
            imageUrl = value.imageUrl,
            description = value.description
        )
}