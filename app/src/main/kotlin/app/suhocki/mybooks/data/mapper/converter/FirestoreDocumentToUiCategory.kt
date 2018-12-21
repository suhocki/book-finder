package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class FirestoreDocumentToUiCategory @Inject constructor(
    private val mapper: FirestoreCategoryToUiCategory
) :
    BaseConverter<DocumentSnapshot, UiCategory>(
        DocumentSnapshot::class.java,
        UiCategory::class.java
    ) {

    override fun convert(value: DocumentSnapshot) =
        value.toObject(FirestoreCategory::class.java)!!
            .apply { id = value.id }
            .let { mapper.convert(it) }
}