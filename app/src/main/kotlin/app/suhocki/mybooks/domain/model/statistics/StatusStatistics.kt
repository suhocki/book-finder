package app.suhocki.mybooks.domain.model.statistics

interface StatusStatistics {
    val category: String
    val status: String
    val count: Int
}