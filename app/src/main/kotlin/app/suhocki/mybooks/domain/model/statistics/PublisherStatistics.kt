package app.suhocki.mybooks.domain.model.statistics

interface PublisherStatistics {
    val category: String
    val publisher: String
    val count: Int
}