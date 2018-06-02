package app.suhocki.mybooks.domain

import android.content.res.Resources
import android.support.annotation.StringRes
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.domain.model.SearchResult
import app.suhocki.mybooks.domain.repository.DatabaseRepository
import javax.inject.Inject

class CategoriesInteractor @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val resourceManager: ResourceManager
) {

    fun getCategories() =
        databaseRepository.getCategories()

    fun getBanner() =
        object : Banner {
            override val description: String
                get() = "ТОЛЬКО В КНИГИ МЫ ВЕРИМ"

            override val imageUrl: String
                get() = "https://mybooks.by/pics/items/3_2.jpg"
        }

    fun search(search: Search) =
        databaseRepository.search(search.searchQuery)
            .map {
                object : SearchResult {
                    override val foundBy = determineFoundBy(search, it)
                    override val book = it
                }
            }
            .toList<SearchResult>()

    private fun determineFoundBy(
        search: Search,
        book: Book
    ): String {
        val q = search.searchQuery
        return when {
            book.productCode.contains(q, true) ->
                composeFindByAndWholeText(R.string.isbn, book.productCode)

            book.fullName.contains(q, true) ->
                composeFindByAndWholeText(R.string.category, book.category)

            book.shortName.contains(q, true) ->
                composeFindByAndWholeText(R.string.category, book.category)

            book.author?.contains(q, true) ?: false ->
                composeFindByAndWholeText(R.string.author, book.author!!)

            book.publisher?.contains(q, true) ?: false ->
                composeFindByAndWholeText(R.string.publisher, book.publisher!!)

            book.year?.contains(q, true) ?: false ->
                composeFindByAndWholeText(R.string.year, book.year!!)

            else -> throw Resources.NotFoundException()
        }
    }

    private fun composeFindByAndWholeText(
        @StringRes foundBy: Int,
        wholeText: String
    ) = "${resourceManager.getString(foundBy)}: $wholeText"
}