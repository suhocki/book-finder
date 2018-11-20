package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.mapper.BaseConverter
import javax.inject.Inject

typealias RoomCategoryEntity = app.suhocki.mybooks.data.room.entity.CategoryEntity
typealias FirestoreCategoryEntity = app.suhocki.mybooks.data.firestore.entity.CategoryEntity

class FirestoreCategoryToRoomCategory @Inject constructor() :
    BaseConverter<FirestoreCategoryEntity, RoomCategoryEntity>(
        FirestoreCategoryEntity::class.java, RoomCategoryEntity::class.java
    ) {

    override fun convert(value: FirestoreCategoryEntity) =
        RoomCategoryEntity(
            value.id,
            value.name,
            value.booksCount
        )
}