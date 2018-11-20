package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.mapper.BaseConverter
import javax.inject.Inject

typealias RoomBookEntity = app.suhocki.mybooks.data.room.entity.BookEntity
typealias FirestoreBookEntity = app.suhocki.mybooks.data.firestore.entity.BookEntity

class FirestoreBookToRoomBook @Inject constructor() :
    BaseConverter<FirestoreBookEntity, RoomBookEntity>(
        FirestoreBookEntity::class.java, RoomBookEntity::class.java
    ) {

    override fun convert(value: FirestoreBookEntity) =
        RoomBookEntity(
            value.categoryId,
            value.shortName,
            value.fullName,
            value.price,
            value.iconLink,
            value.productLink,
            value.id,
            value.website,
            value.status,
            value.publisher,
            value.author,
            value.series,
            value.format,
            value.year,
            value.pageCount,
            value.cover,
            value.description
        )
}