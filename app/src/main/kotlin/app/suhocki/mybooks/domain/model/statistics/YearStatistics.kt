package app.suhocki.mybooks.domain.model.statistics

interface YearStatistics {
    val category: String
    val year: String
    val count: Int
}