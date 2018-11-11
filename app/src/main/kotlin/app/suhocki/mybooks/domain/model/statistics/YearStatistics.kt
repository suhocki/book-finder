package app.suhocki.mybooks.domain.model.statistics

interface YearStatistics {
    val categoryId: String
    val year: String
    val count: Int

    operator fun component1() = categoryId

    operator fun component2() = year

    operator fun component3() = count
}