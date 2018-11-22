package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.mapper.BaseConverter
import javax.inject.Inject

typealias RoomBannerEntity = app.suhocki.mybooks.data.room.entity.BannerEntity
typealias FirestoreBannerEntity = app.suhocki.mybooks.data.firestore.entity.BannerEntity

class FirestoreBannerToRoomBanner @Inject constructor() :
    BaseConverter<FirestoreBannerEntity, RoomBannerEntity>(
        FirestoreBannerEntity::class.java, RoomBannerEntity::class.java
    ) {

    override fun convert(value: FirestoreBannerEntity) =
        RoomBannerEntity(
            value.id,
            value.imageUrl,
            value.description
        )
}