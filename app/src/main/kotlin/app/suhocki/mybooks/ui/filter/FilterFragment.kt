package app.suhocki.mybooks.ui.filter

import android.app.Activity
import android.arch.persistence.db.SupportSQLiteQuery
import android.content.Intent
import android.os.Bundle
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.filter.*
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.listener.OnFilterAuthorClickListener
import app.suhocki.mybooks.ui.base.listener.OnFilterPublisherClickListener
import app.suhocki.mybooks.ui.base.listener.OnFilterResultListener
import app.suhocki.mybooks.ui.filter.listener.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dimenAttr
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.dimen
import org.jetbrains.anko.support.v4.longToast
import toothpick.Toothpick


class FilterFragment : BaseFragment<FilterUI>(), FilterView,
    OnFilterCategoryClickListener,
    OnFilterAuthorClickListener,
    OnFilterPublisherClickListener,
    OnFilterStatusClickListener,
    OnFilterYearClickListener,
    SortNameListener,
    SortPriceListener,
    OnFilterPriceChangeListener {

    override val ui by lazy { FilterUI() }

    private val adapter by lazy {
        FilterAdapter(
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            this
        )
    }

    private var isFilterApplied = false

    @InjectPresenter
    lateinit var presenter: FilterPresenter

    @ProvidePresenter
    fun providePresenter(): FilterPresenter =
        Toothpick.openScopes(DI.APP_SCOPE, DI.BOOKS_SCOPE)
            .getInstance(FilterPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ui.recyclerView.adapter = adapter
        ui.apply.onClick { presenter.applyFilter() }
        ui.reset.onClick {
            presenter.resetFilter()
            (activity as OnFilterResultListener).onFilterReset()
        }
    }

    override fun showFilterItems(
        filterItems: List<Any>,
        toggledCategoryPosition: Int,
        needBottomButtonsUpdate: Boolean
    ) {
        adapter.submitList(filterItems, onAnimationEnd = {
            if (toggledCategoryPosition != UNDEFINED_POSITION) {
                (ui.recyclerView.layoutManager as LinearLayoutManager)
                    .scrollToPositionWithOffset(toggledCategoryPosition, 0)
            }
            if (needBottomButtonsUpdate) presenter.updateBottomButtons()
        })
    }

    override fun showItem(filterItem: Any) {
        val indexToToggle = adapter.items.indexOf(filterItem)
        adapter.notifyItemChanged(indexToToggle)
    }

    override fun showBottomButtonsVisible(configured: Boolean) {
        when {
            !configured && isFilterApplied -> {
                TransitionManager.beginDelayedTransition(ui.bottomPanel, AutoTransition())
                ui.apply.visibility = View.GONE
                ui.buttonsDivider.visibility = View.GONE
                ui.reset.layoutParams = FrameLayout.LayoutParams(
                    dimen(R.dimen.navigation_drawer_width) / 2,
                    ViewGroup.LayoutParams.MATCH_PARENT
                ).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }

            configured && ui.bottomPanel.visibility != View.VISIBLE -> {
                ui.reset.layoutParams = FrameLayout.LayoutParams(
                    dimen(R.dimen.navigation_drawer_width) / 2,
                    ViewGroup.LayoutParams.MATCH_PARENT
                ).apply {
                    gravity = Gravity.END
                }
                ui.apply.visibility = View.VISIBLE
                ui.buttonsDivider.visibility = View.VISIBLE
                ui.bottomPanel.translationY = context!!.dimenAttr(R.attr.actionBarSize).toFloat()
                ui.bottomPanel.visibility = View.VISIBLE
                ViewCompat.animate(ui.bottomPanel).translationY(0f).start()
                ui.recyclerView.bottomPadding = context!!.dimenAttr(R.attr.actionBarSize)
            }

            !configured && ui.bottomPanel.visibility == View.VISIBLE -> {
                ViewCompat.animate(ui.bottomPanel).translationY((ui.bottomPanel.height).toFloat())
                    .withEndAction {
                        ui.bottomPanel.visibility = View.INVISIBLE
                    }
                    .start()
                ui.recyclerView.bottomPadding = 0
            }

            configured && isFilterApplied -> {
                TransitionManager.beginDelayedTransition(ui.bottomPanel, AutoTransition())
                ui.apply.visibility = View.VISIBLE
                ui.buttonsDivider.visibility = View.VISIBLE
                ui.reset.layoutParams = FrameLayout.LayoutParams(
                    dimen(R.dimen.navigation_drawer_width) / 2,
                    ViewGroup.LayoutParams.MATCH_PARENT
                ).apply {
                    gravity = Gravity.END
                }
            }
        }
    }

    override fun onFilterCategoryClick(filterCategory: FilterCategory) {
        if (filterCategory.isExpanded) {
            presenter.collapseFilterCategory(filterCategory, adapter.items)
        } else presenter.expandFilterCategory(filterCategory, adapter.items)
    }

    override fun onFilterAuthorClick(filterAuthor: FilterAuthor) {
        presenter.updateBottomButtons(filterAuthor, adapter.items)
    }

    override fun onFilterYearClick(filterYear: FilterYear) {
        presenter.updateBottomButtons(filterYear, adapter.items)
    }

    override fun onFilterStatusClick(filterStatus: FilterStatus) {
        presenter.updateBottomButtons(filterStatus, adapter.items)
    }

    override fun onFilterPublisherClick(filterPublisher: FilterPublisher) {
        presenter.updateBottomButtons(filterPublisher, adapter.items)
    }

    override fun onSortNameToggle(sortName: SortName) {
        val indexToToggle = adapter.items.indexOf(sortName)
        adapter.notifyItemChanged(indexToToggle)
    }

    override fun onSortNameClick(sortName: SortName) {
        presenter.updateBottomButtons(sortName, adapter.items)
    }

    override fun onSortPriceToggle(sortPrice: SortPrice) {
        val indexToToggle = adapter.items.indexOf(sortPrice)
        adapter.notifyItemChanged(indexToToggle)
    }

    override fun onSortPriceClick(sortPrice: SortPrice) {
        presenter.updateBottomButtons(sortPrice, adapter.items)
    }

//    override fun onExpandSearchClick() {}
//
//    override fun onCollapseSearchClick() = false
//
//    override fun onClearSearchClick() {}
//
//    override fun onStartSearchClick(search: Search) {
//        startActivityForResult<SearchActivity>(
//            ACTIVITY_RESULT_SEARCH,
//            SearchActivity.ARG_SEARCH_KEY to getString(search.hintRes)
//        )
//    }

    override fun showBooks(sqLiteQuery: SupportSQLiteQuery) {
        (activity as OnFilterResultListener).onFilterResult(sqLiteQuery)
    }

    override fun showToast(stringRes: Int) {
        longToast(stringRes)
    }

    override fun onFilterPriceChange(type: FilterPrice.FilterPriceType) {
        presenter.updateBottomButtons()
    }

    override fun setFilterApplied(isFilterApplied: Boolean) {
        this.isFilterApplied = isFilterApplied
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK &&
            requestCode == ACTIVITY_RESULT_SEARCH &&
            data != null) {
//            val payload = data.getParcelableExtra<Parcelable>(SearchActivity.ARG_SEARCH_RESPONSE)
//            val searchKey = data.getStringExtra(SearchActivity.ARG_SEARCH_KEY)
//            presenter.addFilterItem(payload, searchKey, adapter.items)
        }
    }

    companion object {
        const val UNDEFINED_POSITION = -1
        const val ACTIVITY_RESULT_SEARCH = 1024

        fun newInstance() = FilterFragment()
    }
}