package app.suhocki.mybooks.data.mapper.converter

import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.GenericConverter
import app.suhocki.mybooks.ui.catalog.entity.UiCategory
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class FirestoreSnapshotToUiCategories @Inject constructor(
    private val mapper: FirestoreCategoryToUiCategory
) :
    GenericConverter<QuerySnapshot, List<UiCategory>>(
        QuerySnapshot::class.java, ArrayList<UiCategory>().javaClass
    ) {

    override fun convert(value: QuerySnapshot) =
        value.toObjects(FirestoreCategory::class.java)
            .asSequence()
            .apply { forEachIndexed { index, item -> item.id = value.documents[index].id } }
            .mapTo(mutableListOf()) { mapper.convert(it) }

    override val genericType = UiCategory::class.java
}