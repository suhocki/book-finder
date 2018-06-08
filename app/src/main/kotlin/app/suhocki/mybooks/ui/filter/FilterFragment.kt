package app.suhocki.mybooks.ui.filter

import android.app.Activity
import android.arch.persistence.db.SupportSQLiteQuery
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.domain.model.filter.*
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.listener.OnFilterAuthorClickListener
import app.suhocki.mybooks.ui.base.listener.OnFilterPublisherClickListener
import app.suhocki.mybooks.ui.base.listener.OnFilterResultListener
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.filter.listener.*
import app.suhocki.mybooks.ui.search.SearchActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dimenAttr
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivityForResult
import toothpick.Toothpick


class FilterFragment : BaseFragment(), FilterView,
    OnFilterCategoryClickListener,
    OnFilterAuthorClickListener,
    OnFilterPublisherClickListener,
    OnFilterStatusClickListener,
    OnFilterYearClickListener,
    OnSearchClickListener,
    SortNameListener,
    SortPriceListener {

    private val ui by lazy { FilterUI<FilterFragment>() }

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

    @InjectPresenter
    lateinit var presenter: FilterPresenter

    @ProvidePresenter
    fun providePresenter(): FilterPresenter =
        Toothpick.openScopes(DI.APP_SCOPE, DI.BOOKS_ACTIVITY_SCOPE)
            .getInstance(FilterPresenter::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ui.createView(AnkoContext.create(ctx, this@FilterFragment))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ui.recyclerView.adapter = adapter
        ui.apply.setOnClickListener { presenter.applyFilter() }
        ui.reset.setOnClickListener {
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
            if (needBottomButtonsUpdate) presenter.updateBottomButtonsVisibility()
        })
    }

    override fun showItem(filterItem: Any) {
        val indexToToggle = adapter.items.indexOf(filterItem)
        adapter.notifyItemChanged(indexToToggle)
    }

    override fun showBottomButtonsVisible(configured: Boolean) {
        if (configured && ui.bottomPanel.visibility != View.VISIBLE) {
            ui.bottomPanel.translationY = context!!.dimenAttr(R.attr.actionBarSize).toFloat()
            ui.bottomPanel.visibility = View.VISIBLE
            ViewCompat.animate(ui.bottomPanel).translationY(0f).start()
            ui.recyclerView.bottomPadding = context!!.dimenAttr(R.attr.actionBarSize)
        } else if (!configured && ui.bottomPanel.visibility == View.VISIBLE) {
            ViewCompat.animate(ui.bottomPanel).translationY((ui.bottomPanel.height).toFloat())
                .withEndAction { ui.bottomPanel.visibility = View.INVISIBLE }
                .start()
            ui.recyclerView.bottomPadding = 0
        }
    }

    override fun onFilterCategoryClick(filterCategory: FilterCategory) {
        if (filterCategory.isExpanded) {
            presenter.collapseFilterCategory(filterCategory, adapter.items)
        } else presenter.expandFilterCategory(filterCategory, adapter.items)
    }

    override fun onFilterAuthorClick(filterAuthor: FilterAuthor) {
        presenter.updateBottomButtonsVisibility(filterAuthor, adapter.items)
    }

    override fun onFilterYearClick(filterYear: FilterYear) {
        presenter.updateBottomButtonsVisibility(filterYear, adapter.items)
    }

    override fun onFilterStatusClick(filterStatus: FilterStatus) {
        presenter.updateBottomButtonsVisibility(filterStatus, adapter.items)
    }

    override fun onFilterPublisherClick(filterPublisher: FilterPublisher) {
        presenter.updateBottomButtonsVisibility(filterPublisher, adapter.items)
    }

    override fun onSortNameToggle(sortName: SortName) {
        val indexToToggle = adapter.items.indexOf(sortName)
        adapter.notifyItemChanged(indexToToggle)
    }

    override fun onSortNameClick(sortName: SortName) {
        presenter.updateBottomButtonsVisibility(sortName, adapter.items)
    }

    override fun onSortPriceToggle(sortPrice: SortPrice) {
        val indexToToggle = adapter.items.indexOf(sortPrice)
        adapter.notifyItemChanged(indexToToggle)
    }

    override fun onSortPriceClick(sortPrice: SortPrice) {
        presenter.updateBottomButtonsVisibility(sortPrice, adapter.items)
    }

    override fun onExpandSearchClick() {}

    override fun onCollapseSearchClick() = false

    override fun onClearSearchClick() {}

    override fun onStartSearchClick(search: Search) {
        startActivityForResult<SearchActivity>(
            ACTIVITY_RESULT_SEARCH,
            SearchActivity.ARG_SEARCH_KEY to getString(search.hintRes)
        )
    }

    override fun showBooks(sqLiteQuery: SupportSQLiteQuery) {
        (activity as OnFilterResultListener).onFilterResult(sqLiteQuery)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK &&
            requestCode == ACTIVITY_RESULT_SEARCH &&
            data != null) {
            val payload = data.getParcelableExtra<Parcelable>(SearchActivity.ARG_SEARCH_RESPONSE)
            val searchKey = data.getStringExtra(SearchActivity.ARG_SEARCH_KEY)
            presenter.addFilterItem(payload, searchKey, adapter.items)
        }
    }

    companion object {
        const val UNDEFINED_POSITION = -1
        const val ACTIVITY_RESULT_SEARCH = 1024

        fun newInstance() = FilterFragment()
    }
}