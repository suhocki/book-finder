package app.suhocki.mybooks.data.room.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index
import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.domain.model.Book

@Entity(
    indices = [(Index(BookDbo.CATEGORY_ID))],
    tableName = BooksDatabase.Table.BOOKS,
    primaryKeys = [BookDbo.PRODUCT_CODE],
    foreignKeys = [
        (ForeignKey(
            entity = CategoryDbo::class,
            parentColumns = [CategoryDbo.ID],
            childColumns = [BookDbo.CATEGORY_ID]
        ))
    ]
)
data class BookDbo(
    override var id: String = "",
    override var categoryId: String = "",
    override var shortName: String = "",
    override var fullName: String = "",
    override var price: Double = Double.NaN,
    override var iconLink: String = "",
    override var productLink: String = "",
    override var website: String = "",
    override var status: String? = null,
    override var publisher: String? = null,
    override var author: String? = null,
    override var series: String? = null,
    override var format: String? = null,
    override var year: String? = null,
    override var pageCount: String? = null,
    override var cover: String? = null,
    override var description: String? = null,

    var creationDate: String = "",

    @Ignore var shortDescription: String = "",
    @Ignore var fullDescription: String = ""
) : Book {

    companion object {
        const val STATUS = "status"
        const val YEAR = "year"
        const val AUTHOR = "author"
        const val PUBLISHER = "publisher"
        const val SHORT_NAME = "shortName"
        const val PRICE = "price"
        const val CATEGORY_ID = "categoryId"
        const val PRODUCT_CODE = "id"
    }
}