package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.firestore.entity.FirestoreBook
import app.suhocki.mybooks.data.mapper.BaseConverter
import app.suhocki.mybooks.ui.base.entity.UiBook
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class FirestoreDocumentToUiBook @Inject constructor(
    private val mapper: FirestoreBookToUiBook
) :
    BaseConverter<DocumentSnapshot, UiBook>(
        DocumentSnapshot::class.java,
        UiBook::class.java
    ) {

    override fun convert(value: DocumentSnapshot) =
        value.toObject(FirestoreBook::class.java)!!
            .apply { id = value.id }
            .let { mapper.convert(it) }
}