package app.suhocki.mybooks.ui.filter

import android.os.Parcelable
import android.support.annotation.StringRes
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.FilterInteractor
import app.suhocki.mybooks.domain.model.filter.*
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.security.InvalidKeyException
import javax.inject.Inject

@InjectViewState
class FilterPresenter @Inject constructor(
    private val interactor: FilterInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : MvpPresenter<FilterView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            val filterItems = interactor.getFilterCategories()
            uiThread {
                viewState.showFilterItems(filterItems)
            }
        }
    }

    fun collapseFilterCategory(
        filterCategory: FilterCategory,
        items: MutableList<Any>
    ) = doAsync(errorHandler.errorReceiver) {
        val collapsedFilterCategory: FilterCategory = object : FilterCategory {
            override val title = filterCategory.title
            override var isExpanded = false
            override var checkedCount = filterCategory.checkedCount
        }
        interactor.replaceFilterCategoryItem(filterCategory, collapsedFilterCategory)
        val filterCategoryIndex = items.indexOf(filterCategory)
        val filterItems = mutableListOf<Any>().apply {
            addAll(items)
            set(filterCategoryIndex, collapsedFilterCategory)
            removeAll { interactor.isItemUnderCategory(filterCategory, it) }
        }
        uiThread {
            viewState.showFilterItems(filterItems)
        }
    }

    fun expandFilterCategory(
        filterCategory: FilterCategory,
        items: MutableList<Any>
    ) = doAsync(errorHandler.errorReceiver) {
        val filterCategoryIndex = items.indexOf(filterCategory)
        val expandedFilterCategory: FilterCategory = object : FilterCategory {
            override val title = filterCategory.title
            override var isExpanded = true
            override var checkedCount = filterCategory.checkedCount
        }
        interactor.replaceFilterCategoryItem(filterCategory, expandedFilterCategory)
        val filterItems = mutableListOf<Any>().apply {
            addAll(items)
            set(filterCategoryIndex, expandedFilterCategory)
            addAll(
                filterCategoryIndex + 1,
                interactor.getFilterItemsFor(filterCategory)
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
    ) = doAsync(errorHandler.errorReceiver) {
        val updatedList = interactor.addFilterItemToList(filterItem, items, searchKey)
        updateBottomButtons(filterItem, items)
        uiThread { viewState.showFilterItems(updatedList) }
    }

    fun updateBottomButtons() {
        viewState.showBottomButtonsVisible(interactor.isConfigured())
    }

    fun updateBottomButtons(
        item: Any,
        list: MutableList<Any>
    ) = doAsync(errorHandler.errorReceiver) {
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
            viewState.showBottomButtonsVisible(interactor.isConfigured())
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
            interactor.setSortByCheckedCount(sortCategoryTitle, checkedCount)
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
            interactor.incrementCheckedCount()
        } else {
            checkedCount--
            interactor.decrementCheckedCount()
        }
    }

    fun resetFilter() = doAsync(errorHandler.errorReceiver) {
        interactor.reset()
        val filterItems = interactor.getFilterCategories()
        uiThread {
            viewState.showFilterItems(filterItems, needBottomButtonsUpdate = true)
            viewState.setFilterApplied(false)
        }
    }

    fun applyFilter() = doAsync(errorHandler.errorReceiver) {
        if (interactor.validatePriceFilter()) {
            val filterQuery = interactor.getSearchQuery()
            uiThread {
                viewState.showBooks(filterQuery)
                viewState.setFilterApplied(true)
            }
        } else {
            uiThread { viewState.showToast(R.string.error_filter_price_invalid) }
        }
    }
}