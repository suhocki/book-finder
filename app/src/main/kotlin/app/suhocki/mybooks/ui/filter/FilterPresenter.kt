package app.suhocki.mybooks.ui.filter

import android.arch.persistence.db.SupportSQLiteQuery
import android.os.Parcelable
import android.support.annotation.StringRes
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.data.room.BooksDatabase
import app.suhocki.mybooks.data.room.QueryBuilder
import app.suhocki.mybooks.data.room.entity.BookDbo
import app.suhocki.mybooks.di.CategoryId
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.di.SearchAuthor
import app.suhocki.mybooks.di.SearchPublisher
import app.suhocki.mybooks.di.provider.FilterItemStatisticsProvider
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.domain.model.filter.*
import app.suhocki.mybooks.domain.model.statistics.FilterItemStatistics
import app.suhocki.mybooks.ui.filter.entity.EmptyCategoryEntity
import app.suhocki.mybooks.ui.filter.entity.FilterCategoryEntity
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.security.InvalidKeyException
import javax.inject.Inject

@InjectViewState
class FilterPresenter @Inject constructor(
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    private val resourceManager: ResourceManager,
    private val filterItemStatistics: FilterItemStatistics,
    @SearchAuthor private val authorSearchEntity: Search,
    @SearchPublisher private val publisherSearchEntity: Search,
    private val filterPrice: FilterPrice,
    @CategoryId private val categoryId: String
) : MvpPresenter<FilterView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorReceiver) {
            val filterItems = getFilterCategories()
            uiThread {
                viewState.showFilterItems(filterItems)
            }
        }
    }

    fun collapseFilterCategory(
        filterCategory: FilterCategory,
        items: MutableList<Any>
    ) = doAsync(errorReceiver) {
        val collapsedFilterCategory =
            FilterCategoryEntity(filterCategory.title, false, filterCategory.checkedCount)
        replaceFilterCategoryItem(filterCategory, collapsedFilterCategory)
        val filterCategoryIndex = items.indexOf(filterCategory)
        val filterItems = mutableListOf<Any>().apply {
            addAll(items)
            set(filterCategoryIndex, collapsedFilterCategory)
            removeAll { isItemUnderCategory(filterCategory, it) }
        }
        uiThread {
            viewState.showFilterItems(filterItems)
        }
    }

    fun expandFilterCategory(
        filterCategory: FilterCategory,
        items: MutableList<Any>
    ) = doAsync(errorReceiver) {
        val filterCategoryIndex = items.indexOf(filterCategory)
        val expandedFilterCategory =
            FilterCategoryEntity(filterCategory.title, true, filterCategory.checkedCount)
        replaceFilterCategoryItem(filterCategory, expandedFilterCategory)
        val filterItems = mutableListOf<Any>().apply {
            addAll(items)
            set(filterCategoryIndex, expandedFilterCategory)
            addAll(
                filterCategoryIndex + 1,
                getFilterItemsFor(filterCategory)
            )
        }
        uiThread {
            viewState.showFilterItems(filterItems, filterCategoryIndex)
        }
    }

    fun addFilterItem(
        filterItem: Parcelable,
        searchKey: String,
        items: MutableList<Any>
    ) = doAsync(errorReceiver) {
        val updatedList = addFilterItemToList(filterItem, items, searchKey)
        updateBottomButtons(filterItem, items)
        uiThread { viewState.showFilterItems(updatedList) }
    }

    fun updateBottomButtons() {
        viewState.showBottomButtonsVisible(isConfigured())
    }

    fun updateBottomButtons(
        item: Any,
        list: MutableList<Any>
    ) = doAsync(errorReceiver) {
        val filterCategory = when (item) {
            is SortName -> countInvolvedSortItems(R.string.name, list, item)

            is SortPrice -> countInvolvedSortItems(R.string.price, list, item)

            is FilterYear -> countInvolvedFilterItems(R.string.year, list, item)

            is FilterAuthor -> countInvolvedFilterItems(R.string.author, list, item)

            is FilterStatus -> countInvolvedFilterItems(R.string.status, list, item)

            is FilterPublisher -> countInvolvedFilterItems(R.string.publisher, list, item)

            else -> throw InvalidKeyException()
        }
        uiThread {
            viewState.showBottomButtonsVisible(isConfigured())
            viewState.showItem(filterCategory)
        }
    }

    private fun countInvolvedSortItems(
        @StringRes sortCategoryTitle: Int,
        items: MutableList<Any>,
        filterItem: Checkable
    ): FilterCategory {
        return (items.find {
            it is FilterCategory && it.title == resourceManager.getString(sortCategoryTitle)
        } as FilterCategory).apply {
            checkedCount = if (filterItem.isChecked) 1 else 0
            setSortByCheckedCount(sortCategoryTitle, checkedCount)
        }
    }

    private fun countInvolvedFilterItems(
        @StringRes filterCategoryTitle: Int,
        items: MutableList<Any>,
        filterItem: Any
    ): FilterCategory = (items.find {
        it is FilterCategory && it.title == resourceManager.getString(filterCategoryTitle)
    } as FilterCategory).apply {
        if ((filterItem as Checkable).isChecked) {
            checkedCount++
            incrementCheckedCount()
        } else {
            checkedCount--
            decrementCheckedCount()
        }
    }

    fun resetFilter() = doAsync(errorReceiver) {
        reset()
        val filterItems = getFilterCategories()
        uiThread {
            viewState.showFilterItems(filterItems, needBottomButtonsUpdate = true)
            viewState.setFilterApplied(false)
        }
    }

    fun applyFilter() = doAsync(errorReceiver) {
        if (validatePriceFilter()) {
            val filterQuery = getSearchQuery()
            uiThread {
                viewState.showBooks(filterQuery)
                viewState.setFilterApplied(true)
            }
        } else {
            uiThread { viewState.showToast(R.string.error_filter_price_invalid) }
        }
    }

    private fun getFilterCategories() = filterItemStatistics.filterCategories

    private fun isItemUnderCategory(
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
            item is FilterYear || (item is EmptyCategory && item.categoryTitle == category.title)

        resourceManager.getString(R.string.status) ->
            item is FilterStatus

        else -> throw InvalidKeyException()
    }

    private fun getFilterItemsFor(
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
                addAll(filterItemStatistics.authorsFilterItems)
                add(authorSearchEntity)
            }

        resourceManager.getString(R.string.publisher) ->
            mutableListOf<Any>().apply {
                addAll(filterItemStatistics.publishersFilterItems)
                add(publisherSearchEntity)
            }

        resourceManager.getString(R.string.price) ->
            mutableListOf<Any>().apply {
                filterPrice.hintFrom; filterPrice.hintTo
                add(filterPrice)
                addAll(filterItemStatistics.pricesSortItems)
            }

        else -> throw InvalidKeyException()
    }.let {
        if (it.isEmpty()) mutableListOf<Any>().apply {
            add(EmptyCategoryEntity(filterCategory, resourceManager))
        }
        else it
    }

    private fun addFilterItemToList(
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
                    val indexToInsert = items.indexOf(searchEntity)
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
                    val indexToInsert = items.indexOf(searchEntity)
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

    private fun setSortByCheckedCount(@StringRes sortCategoryTitle: Int, checkedCount: Int) {
        filterItemStatistics.checkedSortByCategory[sortCategoryTitle] = checkedCount
    }

    private fun incrementCheckedCount() = filterItemStatistics.checkedItemCount++

    private fun decrementCheckedCount() = filterItemStatistics.checkedItemCount--

    private fun isConfigured() = filterItemStatistics.checkedItemCount > 0 ||
            filterItemStatistics.checkedSortByCategory.containsValue(1) ||
            filterPrice.from > 0.0 ||
            filterPrice.to < Integer.MAX_VALUE

    private fun reset() = with(filterItemStatistics) {
        filterPrice.from = 0.0
        filterPrice.to = Integer.MAX_VALUE.toDouble()
        pricesSortItems.forEach { it.isChecked = false }
        authorsFilterItems.forEach { it.isChecked = false }
        publishersFilterItems.forEach { it.isChecked = false }
        yearsFilterItems.forEach { it.isChecked = false }
        statusesFilterItems.forEach { it.isChecked = false }
        nameSortItems.forEach { it.isChecked = false }
        checkedItemCount = 0
        checkedSortByCategory.clear()
        filterCategories.forEach {
            it.checkedCount = 0
            it.isExpanded = false
        }
    }

    private fun replaceFilterCategoryItem(old: FilterCategory, new: FilterCategory) {
        with(filterItemStatistics.filterCategories) {
            val oldInList = find { it.title == old.title }
            val itemIndex = indexOf(oldInList)
            set(itemIndex, new)
        }
    }

    private fun getSearchQuery(): SupportSQLiteQuery {
        val builder = QueryBuilder.builder(BooksDatabase.Table.BOOKS)
            .selection("${BookDbo.CATEGORY_ID} = ?", arrayOf(categoryId))
        with(filterItemStatistics) {

            nameSortItems.find { it.isChecked }?.let {
                builder.setFirstOrderBy(BookDbo.SHORT_NAME)
                when (it.sortName) {
                    resourceManager.getString(R.string.ascending) -> QueryBuilder.ORDER_TYPE_ASC

                    resourceManager.getString(R.string.descending) -> QueryBuilder.ORDER_TYPE_DESC

                    else -> throw InvalidKeyException()
                }.let { builder.setFirstOrderType(it) }
            }

            builder.filter(
                BookDbo.PRICE,
                resourceManager.getString(R.string.format_two_sign_after_point, filterPrice.from)
                    .replace(",", "."),
                resourceManager.getString(R.string.format_two_sign_after_point, filterPrice.to)
                    .replace(",", ".")
            )

            pricesSortItems.find { it.isChecked }?.let {
                builder.setSecondOrderBy(BookDbo.PRICE)
                when (it.sortName) {
                    resourceManager.getString(R.string.ascending) -> QueryBuilder.ORDER_TYPE_ASC

                    resourceManager.getString(R.string.descending) -> QueryBuilder.ORDER_TYPE_DESC

                    else -> throw InvalidKeyException()
                }.let { builder.setSecondOrderType(it) }
            }

            val checkedStatuses = statusesFilterItems.filter { it.isChecked }
            if (checkedStatuses.isNotEmpty()) {
                val query = StringBuilder(120)
                query.append("(")
                checkedStatuses.forEachIndexed { index, filterStatus ->
                    query.append("'${filterStatus.status}'")
                    if (checkedStatuses.lastIndex != index) query.append(", ")
                }
                query.append(")")
                builder.statusIn(BookDbo.STATUS, query.toString())
            }

            val checkedYears = yearsFilterItems.filter { it.isChecked }
            if (checkedYears.isNotEmpty()) {
                val query = StringBuilder(120)
                query.append("(")
                checkedYears.forEachIndexed { index, filterYear ->
                    query.append("'${filterYear.year}'")
                    if (checkedYears.lastIndex != index) query.append(", ")
                }
                query.append(")")
                builder.yearIn(BookDbo.YEAR, query.toString())
            }

            val checkedAuthors = authorsFilterItems.filter { it.isChecked }
            if (checkedAuthors.isNotEmpty()) {
                val query = StringBuilder(120)
                query.append("(")
                checkedAuthors.forEachIndexed { index, filterAuthor ->
                    query.append("'${filterAuthor.authorName}'")
                    if (checkedAuthors.lastIndex != index) query.append(", ")
                }
                query.append(")")
                builder.authorIn(BookDbo.AUTHOR, query.toString())
            }

            val checkedPublishers = publishersFilterItems.filter { it.isChecked }
            if (checkedPublishers.isNotEmpty()) {
                val query = StringBuilder(120)
                query.append("(")
                checkedPublishers.forEachIndexed { index, filterPublisher ->
                    query.append("'${filterPublisher.publisherName}'")
                    if (checkedPublishers.lastIndex != index) query.append(", ")
                }
                query.append(")")
                builder.publisherIn(BookDbo.PUBLISHER, query.toString())
            }
        }
        return builder.create()
    }

    private fun validatePriceFilter() = filterPrice.from <= filterPrice.to
}