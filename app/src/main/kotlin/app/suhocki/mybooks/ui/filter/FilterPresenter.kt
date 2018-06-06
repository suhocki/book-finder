package app.suhocki.mybooks.ui.filter

import android.os.Parcelable
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.FilterInteractor
import app.suhocki.mybooks.domain.model.filter.*
import app.suhocki.mybooks.domain.model.statistics.FilterItemStatistics
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class FilterPresenter @Inject constructor(
    private val interactor: FilterInteractor,
    private val errorHandler: ErrorHandler,
    private val filterItemStatistics: FilterItemStatistics,
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
        val filterCategoryIndex = items.indexOf(filterCategory)
        val filterItems = mutableListOf<Any>().apply {
            addAll(items)
            set(filterCategoryIndex, object : FilterCategory {
                override val title = filterCategory.title
                override var isExpanded = false
                override var checkedCount = filterCategory.checkedCount
            })
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
        val filterItems = mutableListOf<Any>().apply {
            addAll(items)
            set(filterCategoryIndex, object : FilterCategory {
                override val title = filterCategory.title
                override var isExpanded = true
                override var checkedCount = filterCategory.checkedCount
            })
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
        uiThread { viewState.showFilterItems(updatedList) }
    }

    fun onFilterItemStateChanged(
        filterItem: Any,
        items: MutableList<Any>
    ) = doAsync(errorHandler.errorReceiver) {
        var filterCategory: FilterCategory? = null
        when (filterItem) {
            is SortName -> {
                filterCategory = items.find {
                    it is FilterCategory && it.title == resourceManager.getString(R.string.name)
                } as FilterCategory
                if (filterItem.isChecked) filterCategory.checkedCount = 1
                else filterCategory.checkedCount = 0
            }

            is SortPrice -> {
                filterCategory = items.find {
                    it is FilterCategory && it.title == resourceManager.getString(R.string.price)
                } as FilterCategory
                if (filterItem.isChecked) filterCategory.checkedCount = 1
                else filterCategory.checkedCount = 0
            }

            is FilterYear -> {
                filterCategory = items.find {
                    it is FilterCategory && it.title == resourceManager.getString(R.string.year)
                } as FilterCategory
                if (filterItem.isChecked) filterCategory.checkedCount++
                else filterCategory.checkedCount--
            }

            is FilterAuthor -> {
                filterCategory = items.find {
                    it is FilterCategory && it.title == resourceManager.getString(R.string.author)
                } as FilterCategory
                if (filterItem.isChecked) filterCategory.checkedCount++
                else filterCategory.checkedCount--
            }

            is FilterStatus -> {
                filterCategory = items.find {
                    it is FilterCategory && it.title == resourceManager.getString(R.string.status)
                } as FilterCategory
                if (filterItem.isChecked) filterCategory.checkedCount++
                else filterCategory.checkedCount--
            }

            is FilterPublisher -> {
                filterCategory = items.find {
                    it is FilterCategory && it.title == resourceManager.getString(R.string.publisher)
                } as FilterCategory
                if (filterItem.isChecked) filterCategory.checkedCount++
                else filterCategory.checkedCount--
            }
        }
        uiThread { viewState.showItem(filterCategory!!) }
    }
}