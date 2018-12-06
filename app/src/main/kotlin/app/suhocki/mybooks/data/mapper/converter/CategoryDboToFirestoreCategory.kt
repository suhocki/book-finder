package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.data.room.entity.CategoryDbo
import javax.inject.Inject

class CategoryDboToFirestoreCategory @Inject constructor() :
    BaseConverter<CategoryDbo, FirestoreCategory>(
        CategoryDbo::class.java, FirestoreCategory::class.java
    ) {

    override fun convert(value: CategoryDbo) =
        FirestoreCategory(
            id = value.id,
            name = value.name,
            booksCount = value.booksCount
        )
}