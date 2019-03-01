package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.firestore.entity.FirestoreBanner
import app.suhocki.mybooks.data.firestore.entity.FirestoreBook
import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.ui.base.entity.UiBook
import app.suhocki.mybooks.ui.catalog.entity.UiBanner
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class FirestoreConverters {

    //region FirestoreEntities converters
    class FirestoreCategoryToCategory @Inject constructor() :
        BaseConverter<FirestoreCategory, UiCategory>(
            FirestoreCategory::class.java,
            UiCategory::class.java
        ) {
        override fun convert(
            value: FirestoreCategory
        ) = UiCategory(
            id = value.id,
            name = value.name,
            booksCount = value.booksCount
        )
    }

    class FirestoreBookToBook @Inject constructor() :
        BaseConverter<FirestoreBook, UiBook>(
            FirestoreBook::class.java,
            UiBook::class.java
        ) {
        override fun convert(
            value: FirestoreBook
        ) = UiBook(
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

    class FirestoreBannerToUiBanner @Inject constructor() :
        BaseConverter<FirestoreBanner, UiBanner>(
            FirestoreBanner::class.java,
            UiBanner::class.java
        ) {
        override fun convert(
            value: FirestoreBanner
        ) = UiBanner(
            id = value.id,
            imageUrl = value.imageUrl,
            description = value.description
        )
    }
    //endregion

    //region DocumentSnapshot converters
    class DocumentSnapshotToCategory @Inject constructor(
        private val mapper: FirestoreCategoryToCategory
    ) : BaseConverter<DocumentSnapshot, UiCategory>(
        DocumentSnapshot::class.java,
        UiCategory::class.java
    ) {
        override fun convert(value: DocumentSnapshot) =
            value.toObject(FirestoreCategory::class.java)!!
                .apply { id = value.id }
                .let { mapper.convert(it) }
    }

    class DocumentSnapshotToBook @Inject constructor(
        private val mapper: FirestoreBookToBook
    ) : BaseConverter<DocumentSnapshot, UiBook>(
        DocumentSnapshot::class.java,
        UiBook::class.java
    ) {
        override fun convert(value: DocumentSnapshot) =
            value.toObject(FirestoreBook::class.java)!!
                .apply { id = value.id }
                .let { mapper.convert(it) }
    }

    class DocumentSnapshotToBanner @Inject constructor(
        private val mapper: FirestoreBannerToUiBanner
    ) : BaseConverter<DocumentSnapshot, UiBanner>(
        DocumentSnapshot::class.java,
        UiBanner::class.java
    ) {
        override fun convert(value: DocumentSnapshot) =
            value.toObject(FirestoreBanner::class.java)!!
                .apply { id = value.id }
                .let { mapper.convert(it) }
    }
    //endregion
}
