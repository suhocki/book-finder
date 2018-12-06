package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.data.room.entity.CategoryDbo
import javax.inject.Inject

class FirestoreCategoryToCategoryDbo @Inject constructor() :
    BaseConverter<FirestoreCategory, CategoryDbo>(
        FirestoreCategory::class.java, CategoryDbo::class.java
    ) {

    override fun convert(value: FirestoreCategory) =
        CategoryDbo(
            id = value.id,
            name = value.name,
            booksCount = value.booksCount
        )
}