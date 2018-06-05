package app.suhocki.mybooks.ui.filter

import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.domain.FilterInteractor
import app.suhocki.mybooks.domain.model.filter.FilterCategory
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class FilterPresenter @Inject constructor(
    private val filterInteractor: FilterInteractor,
    private val errorHandler: ErrorHandler
) : MvpPresenter<FilterView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            val filterItems = filterInteractor.getFilterCategories().toList()
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
                override var isConfigurated = filterCategory.isConfigurated
            })
            removeAll { filterInteractor.isItemUnderCategory(filterCategory, it) }
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
                override var isConfigurated = filterCategory.isConfigurated
            })
            addAll(
                filterCategoryIndex + 1,
                filterInteractor.getFilterItemsFor(filterCategory)
            )
        }
        uiThread {
            viewState.showFilterItems(filterItems)
        }
    }
}