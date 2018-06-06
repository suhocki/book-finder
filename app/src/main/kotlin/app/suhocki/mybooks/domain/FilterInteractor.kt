package app.suhocki.mybooks.domain

import android.os.Parcelable
import android.support.annotation.StringRes
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.SearchAuthor
import app.suhocki.mybooks.di.SearchPublisher
import app.suhocki.mybooks.di.provider.FilterItemStatisticsProvider
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.domain.model.filter.*
import app.suhocki.mybooks.domain.model.statistics.FilterItemStatistics
import java.security.InvalidKeyException
import javax.inject.Inject

class FilterInteractor @Inject constructor(
    private val filterItemStatistics: FilterItemStatistics,
    private val resourceManager: ResourceManager,
    @SearchAuthor private val authorSearchEntity: Search,
    @SearchPublisher private val publisherSearchEntity: Search,
    private val filterPrice: FilterPrice
) {

    fun getFilterCategories() = filterItemStatistics.filterCategories

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

        resourceManager.getString(R.string.status) ->
            item is FilterStatus

        else -> throw InvalidKeyException()
    }

    fun getFilterItemsFor(
        filterCategory: FilterCategory
    ): Collection<Any> = when (filterCategory.title) {

        resourceManager.getString(R.string.name) ->
            filterItemStatistics.nameSortItems

        resourceManager.getString(R.string.year) ->
            filterItemStatistics.yearsFilterItems

        resourceManager.getString(R.string.status) ->
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
                filterPrice.hintFrom; filterPrice.hintTo
                add(filterPrice)
                addAll(filterItemStatistics.filterByPriceItems)
            }

        else -> throw InvalidKeyException()
    }

    fun addFilterItemToList(
        filterItem: Parcelable,
        items: MutableList<Any>,
        searchKey: String
    ): List<Any> {
        val newList = mutableListOf<Any>().apply { addAll(items) }

        when (searchKey) {
            resourceManager.getString(R.string.hint_search_author) -> {
                filterItem as FilterAuthor
                if (filterItemStatistics.authorsFilterItems.contains(filterItem)) {
                    val index = items.indexOf(filterItem)
                    val oldValue = items[index] as FilterAuthor
                    val newValue = FilterItemStatisticsProvider.FilterAuthorEntity(
                        oldValue.authorName,
                        oldValue.booksCount,
                        isChecked = true
                    )
                    newList[index] = newValue

                    val index1 = filterItemStatistics.authorsFilterItems.indexOf(filterItem)
                    filterItemStatistics.authorsFilterItems[index1] = newValue

                } else {
                    filterItemStatistics.authorsFilterItems.add(0, filterItem)
                    val searchEntity = items.find {
                        it is Search && it.hintRes == R.string.hint_search_author
                    }
                    val indexToInsert = items.indexOf(searchEntity) + 1
                    newList.add(indexToInsert, filterItem.apply {
                        isChecked = true
                        isCheckable = true
                    })
                }
            }

            resourceManager.getString(R.string.hint_search_publisher) -> {
                filterItem as FilterPublisher
                if (filterItemStatistics.publishersFilterItems.contains(filterItem)) {
                    val index = items.indexOf(filterItem)
                    val oldValue = items[index] as FilterPublisher
                    val newValue = FilterItemStatisticsProvider.FilterPublisherEntity(
                        oldValue.publisherName,
                        oldValue.booksCount,
                        isChecked = true
                    )
                    newList[index] = newValue

                    val index1 = filterItemStatistics.publishersFilterItems.indexOf(filterItem)
                    filterItemStatistics.publishersFilterItems[index1] = newValue

                } else {
                    filterItemStatistics.publishersFilterItems.add(0, filterItem)
                    val searchEntity = items.find {
                        it is Search && it.hintRes == R.string.hint_search_publisher
                    }
                    val indexToInsert = items.indexOf(searchEntity) + 1
                    newList.add(indexToInsert, filterItem.apply {
                        isChecked = true
                        isCheckable = true
                    })
                }
            }

            else -> throw InvalidKeyException()
        }

        return newList
    }

    fun setSortByCheckedCount(@StringRes sortCategoryTitle: Int, checkedCount: Int) {
        filterItemStatistics.checkedSortByCategory[sortCategoryTitle] = checkedCount
    }

    fun incrementCheckedCount() = filterItemStatistics.checkedItemCount++

    fun decrementCheckedCount() = filterItemStatistics.checkedItemCount--

    fun isConfigured()  = filterItemStatistics.checkedItemCount > 0 ||
            filterItemStatistics.checkedSortByCategory.containsValue(1)
}