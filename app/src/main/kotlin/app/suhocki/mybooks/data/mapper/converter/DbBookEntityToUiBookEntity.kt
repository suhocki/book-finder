package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.mapper.BaseConverter
import javax.inject.Inject

typealias DatabaseBookEntity = app.suhocki.mybooks.data.room.entity.BookEntity
typealias UiBookEntity = app.suhocki.mybooks.ui.base.entity.BookEntity

class DbBookEntityToUiBookEntity @Inject constructor() :
    BaseConverter<DatabaseBookEntity, UiBookEntity>(
        DatabaseBookEntity::class.java, UiBookEntity::class.java
    ) {

    override fun convert(value: DatabaseBookEntity) =
        UiBookEntity(
            R.drawable.ic_buy,
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