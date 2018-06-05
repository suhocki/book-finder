package app.suhocki.mybooks.domain

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.SearchAuthor
import app.suhocki.mybooks.di.SearchPublisher
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.domain.model.filter.*
import app.suhocki.mybooks.domain.model.statistics.FilterItemStatistics
import app.suhocki.mybooks.domain.repository.FilterRepository
import java.security.InvalidKeyException
import javax.inject.Inject

class FilterInteractor @Inject constructor(
    private val filterRepository: FilterRepository,
    private val filterItemStatistics: FilterItemStatistics,
    private val resourceManager: ResourceManager,
    @SearchAuthor private val authorSearchEntity: Search,
    @SearchPublisher private val publisherSearchEntity: Search,
    private val filterPrice: FilterPrice
) {

    fun getFilterCategories() =
        filterRepository.getFilterCategories()

    fun isItemUnderCategory(
        category: FilterCategory,
        item: Any
    ): Boolean = when (category.title) {
        resourceManager.getString(R.string.price) ->
            item is SortPrice || item is FilterPrice

        resourceManager.getString(R.string.name) ->
            item is SortName

        resourceManager.getString(R.string.author) ->
            item is FilterAuthor ||
                    (item is Search && item.hintRes == R.string.hint_search_author)

        resourceManager.getString(R.string.publisher) ->
            item is FilterPublisher ||
                    (item is Search && item.hintRes == R.string.hint_search_publisher)

        resourceManager.getString(R.string.year) ->
            item is FilterYear

        resourceManager.getString(R.string.availability) ->
            item is FilterStatus

        else -> throw InvalidKeyException()
    }

    fun getFilterItemsFor(
        filterCategory: FilterCategory
    ): Collection<Any> = when (filterCategory.title) {

        resourceManager.getString(R.string.name) ->
            filterRepository.getFilterByNameItems()

        resourceManager.getString(R.string.year) ->
            filterItemStatistics.yearsFilterItems

        resourceManager.getString(R.string.availability) ->
            filterItemStatistics.statusesFilterItems

        resourceManager.getString(R.string.author) ->
            mutableListOf<Any>().apply {
                add(authorSearchEntity)
                addAll(filterItemStatistics.authorsFilterItems)
            }

        resourceManager.getString(R.string.publisher) ->
            mutableListOf<Any>().apply {
                add(publisherSearchEntity)
                addAll(filterItemStatistics.publishersFilterItems)
            }

        resourceManager.getString(R.string.price) ->
            mutableListOf<Any>().apply {
                add(filterPrice)
                addAll(filterRepository.getFilterByPriceItems())
            }

        else -> throw InvalidKeyException()
    }
}