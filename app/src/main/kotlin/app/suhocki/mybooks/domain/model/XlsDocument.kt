package app.suhocki.mybooks.domain.model

import app.suhocki.mybooks.data.parser.entity.StatisticsEntity

interface XlsDocument {
    val title: String
    val creationDate: String
    val columnNames: List<String>
    val booksData: Map<out Category, Collection<Book>>
    val statisticsData: Map<Category, StatisticsEntity>
    val shopInfo: ShopInfo
    val bannersData: List<Banner>
}