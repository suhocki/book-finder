package app.suhocki.mybooks.domain

import android.content.res.Resources
import android.support.annotation.StringRes
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.data.remoteconfig.RemoteConfiguration
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.Room
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.domain.model.SearchResult
import app.suhocki.mybooks.domain.repository.BannersRepository
import app.suhocki.mybooks.domain.repository.BooksRepository
import app.suhocki.mybooks.ui.base.entity.BookEntity
import javax.inject.Inject

class CatalogInteractor @Inject constructor(
    @Room private val localBooksRepository: BooksRepository,
    private val remoteConfigurator: RemoteConfiguration,
    private val adsManager: AdsManager,
    private val bannersRepository: BannersRepository,
    private val resourceManager: ResourceManager
) {

    fun getCategories() =
        localBooksRepository.getCategories()

    fun getBanner(): Any =
        if (remoteConfigurator.isBannerAdEnabled) adsManager.getBannerAd()
        else bannersRepository.getBanners().first()

    fun search(search: Search) =
        localBooksRepository.search(search.searchQuery)
            .map {
                object : SearchResult {
                    override val foundBy = determineFoundBy(search, it)
                    override val book = BookEntity(it)
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