package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.mapper.BaseConverter
import javax.inject.Inject

typealias FirestoreCategory = app.suhocki.mybooks.data.firestore.entity.CategoryEntity
typealias RoomCategory = app.suhocki.mybooks.data.room.entity.CategoryEntity

class FirestoreCategoryToRoomCategory @Inject constructor() :
    BaseConverter<FirestoreCategory, RoomCategory>(
        FirestoreCategory::class.java, RoomCategory::class.java
    ) {

    override fun convert(value: FirestoreCategory) =
        RoomCategory(value.id, value.name, value.booksCount)
}