package app.suhocki.mybooks.domain.model.statistics

interface StatusStatistics {
    val category: String
    val status: String
    val count: Int

    operator fun component1() = category

    operator fun component2() = category

    operator fun component3() = count
}