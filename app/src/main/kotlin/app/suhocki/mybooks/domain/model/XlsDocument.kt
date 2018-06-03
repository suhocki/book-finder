package app.suhocki.mybooks.domain.model

import app.suhocki.mybooks.domain.model.statistics.Statistics

interface XlsDocument {
    val title: String
    val creationDate: String
    val columnNames: List<String>
    val data: Map<out Category, Collection<Book>>
    val statistics: Statistics
}