package app.suhocki.mybooks.domain.model.statistics

interface YearStatistics {
    val category: String
    val year: String
    val count: Int

    operator fun component1() = category

    operator fun component2() = year

    operator fun component3() = count
}