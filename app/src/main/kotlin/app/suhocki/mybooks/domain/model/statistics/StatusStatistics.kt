package app.suhocki.mybooks.domain.model.statistics

interface StatusStatistics {
    val categoryId: String
    val status: String
    val count: Int

    operator fun component1() = categoryId

    operator fun component2() = status

    operator fun component3() = count
}