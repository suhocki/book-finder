package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.firestore.entity.FirestoreBook
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.ui.base.entity.UiBook
import javax.inject.Inject

class FirestoreBookToUiBook @Inject constructor() :
    BaseConverter<FirestoreBook, UiBook>(
        FirestoreBook::class.java, UiBook::class.java
    ) {

    override fun convert(value: FirestoreBook) =
        UiBook(
            buyDrawableRes = 0,
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