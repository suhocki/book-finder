package app.suhocki.mybooks.data.parser.entity

import app.suhocki.mybooks.data.database.entity.BookEntity
import app.suhocki.mybooks.data.database.entity.CategoryEntity
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.XlsDocument

data class XlsDocumentEntity(
    override val title: String,
    override val creationDate: String,
    override val columnNames: List<String>,
    override val booksData: Map<CategoryEntity, Collection<BookEntity>>,
    override val statisticsData: Map<Category, StatisticsEntity>
) : XlsDocument