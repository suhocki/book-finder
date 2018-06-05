package app.suhocki.mybooks.data.parser.entity

import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.statistics.Statistics

class StatisticsEntity(
    override val authors: MutableMap<String, Int> = mutableMapOf(),
    override val publishers: MutableMap<String, Int> = mutableMapOf(),
    override val years: MutableMap<String, Int> = mutableMapOf(),
    override val statuses: MutableMap<String, Int> = mutableMapOf(),
    override val prices: DoubleArray = doubleArrayOf(Double.MAX_VALUE, Double.MIN_VALUE)
) : Statistics {
    fun add(book: Book) {
        book.author?.let {
            var authorsCount = authors.getOrDefault(it, 0)
            authors.put(it, ++authorsCount)
        }
        book.publisher?.let {
            var publishersCount = publishers.getOrDefault(it, 0)
            publishers.put(it, ++publishersCount)
        }
        book.year?.let {
            var yearsCount = years.getOrDefault(it, 0)
            years.put(it, ++yearsCount)
        }
        book.status.let {
            var statusesCount = statuses.getOrDefault(it, 0)
            statuses.put(it, ++statusesCount)
        }
        book.price.let {
            val (minPrice, maxPrice) = prices
            if (minPrice > it) prices[0] = it
            if (maxPrice < it) prices[1] = it
        }
    }
}