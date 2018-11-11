package app.suhocki.mybooks.domain.model.statistics

interface PriceStatistics {
    val categoryId: String
    val minPrice: Double
    val maxPrice: Double

    operator fun component1() = categoryId

    operator fun component2() = minPrice

    operator fun component3() = maxPrice
}