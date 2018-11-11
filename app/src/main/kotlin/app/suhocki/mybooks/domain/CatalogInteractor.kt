package app.suhocki.mybooks.domain

import android.content.res.Resources
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

    fun getBanner(): Any? =
        if (remoteConfigurator.isBannerAdEnabled) adsManager.getBannerAd()
        else bannersRepository.getBanners().firstOrNull()

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
                "${resourceManager.getString(R.string.isbn)}: ${book.productCode}"

            book.fullName.contains(q, true) ->
                "${resourceManager.getString(R.string.category)}: ${book.categoryId}"

            book.shortName.contains(q, true) ->
                "${resourceManager.getString(R.string.category)}: ${book.categoryId}"

            book.author?.contains(q, true) ?: false ->
                "${resourceManager.getString(R.string.author)}: ${book.author}"

            book.publisher?.contains(q, true) ?: false ->
                "${resourceManager.getString(R.string.publisher)}: ${book.publisher}"

            book.year?.contains(q, true) ?: false ->
                "${resourceManager.getString(R.string.year)}: ${book.year}"

            else -> throw Resources.NotFoundException()
        }
    }
}