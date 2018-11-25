package app.suhocki.mybooks.data.firestore.entity

import app.suhocki.mybooks.domain.model.Book

class FirestoreBook(
    override var categoryId: String = "",
    override var shortName: String = "",
    override var fullName: String = "",
    override var price: Double = 0.0,
    override var iconLink: String = "",
    override var productLink: String = "",
    override var id: String = "",
    override var website: String = "",
    override var status: String? = null,
    override var publisher: String? = null,
    override var author: String? = null,
    override var series: String? = null,
    override var format: String? = null,
    override var year: String? = null,
    override var pageCount: String? = null,
    override var cover: String? = null,
    override var description: String? = null
) : Book {
    constructor() : this(categoryId = "")
}