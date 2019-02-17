package app.suhocki.mybooks.ui.base.entity

import app.suhocki.mybooks.domain.model.Book

class UiBook(
    var buyDrawableRes: Int,

    override val categoryId: String,
    override val shortName: String,
    override val fullName: String,
    override val price: Double,
    override val iconLink: String,
    override val productLink: String,
    override val id: String,
    override val website: String,
    override val status: String?,
    override val publisher: String?,
    override val author: String?,
    override val series: String?,
    override val format: String?,
    override val year: String?,
    override val pageCount: String?,
    override val cover: String?,
    override val description: String?
) : Book
