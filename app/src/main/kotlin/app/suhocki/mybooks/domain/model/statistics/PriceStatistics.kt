package app.suhocki.mybooks.domain.model.statistics

interface PriceStatistics {
    val category: String
    val minPrice: Double
    val maxPrice: Double

    operator fun component1() = category

    operator fun component2() = minPrice

    operator fun component3() = maxPrice
}