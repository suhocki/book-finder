package app.suhocki.mybooks.domain.model.statistics

interface PublisherStatistics {
    val category: String
    val publisher: String
    val count: Int

    operator fun component1() = category

    operator fun component2() = publisher

    operator fun component3() = count
}