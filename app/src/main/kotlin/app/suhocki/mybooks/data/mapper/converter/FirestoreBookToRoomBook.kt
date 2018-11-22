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
            categoryId = value.categoryId,
            shortName = value.shortName,
            fullName = value.fullName,
            price = value.price,
            iconLink = value.iconLink,
            productLink = value.productLink,
            website = value.website,
            id = value.id,
            status = value.status,
            publisher = value.publisher,
            author = value.author,
            series = value.series,
            format = value.format,
            year = value.year,
            pageCount = value.pageCount,
            cover = value.cover,
            description = value.description
        )
}