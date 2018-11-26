package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import javax.inject.Inject

class FirestoreCategoryToUiCategory @Inject constructor() :
    BaseConverter<FirestoreCategory, UiCategory>(
        FirestoreCategory::class.java, UiCategory::class.java
    ) {

    override fun convert(value: FirestoreCategory) =
        UiCategory(
            id = value.id,
            name = value.name,
            booksCount = value.booksCount
        )
}