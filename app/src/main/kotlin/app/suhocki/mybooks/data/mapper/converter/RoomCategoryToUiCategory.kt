package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.data.room.entity.CategoryDbo
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import javax.inject.Inject

class RoomCategoryToUiCategory @Inject constructor() :
    BaseConverter<CategoryDbo, UiCategory>(
        CategoryDbo::class.java, UiCategory::class.java
    ) {

    override fun convert(value: CategoryDbo) =
        UiCategory(
            id = value.id,
            name = value.name,
            booksCount = value.booksCount
        )
}