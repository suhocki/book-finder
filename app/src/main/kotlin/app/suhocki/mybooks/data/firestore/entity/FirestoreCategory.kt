package app.suhocki.mybooks.data.firestore.entity

import app.suhocki.mybooks.domain.model.Category

class FirestoreCategory(
    override var id: String = "",
    override var name: String = "",
    override var booksCount: Int = 0
) : Category {
    constructor() : this(id = "")
}