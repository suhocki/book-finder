package app.suhocki.mybooks.domain.model.statistics

interface AuthorStatistics {
    val categoryId: String
    val author: String
    val count: Int

    operator fun component1() = categoryId

    operator fun component2() = author

    operator fun component3() = count
}