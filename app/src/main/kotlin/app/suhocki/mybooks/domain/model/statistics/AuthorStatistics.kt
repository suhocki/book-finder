package app.suhocki.mybooks.domain.model.statistics

interface AuthorStatistics {
    val category: String
    val author: String
    val count: Int
}