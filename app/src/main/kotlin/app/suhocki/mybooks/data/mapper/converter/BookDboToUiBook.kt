package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.data.room.entity.BookDbo
import app.suhocki.mybooks.ui.base.entity.UiBook
import javax.inject.Inject

class BookDboToUiBook @Inject constructor() :
    BaseConverter<BookDbo, UiBook>(
        BookDbo::class.java, UiBook::class.java
    ) {

    override fun convert(value: BookDbo) =
        UiBook(
            buyDrawableRes = R.drawable.ic_buy,
            categoryId = value.categoryId,
            shortName = value.shortName,
            fullName = value.fullName,
            price = value.price,
            iconLink = value.iconLink,
            productLink = value.productLink,
            id = value.id,
            website = value.website,
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