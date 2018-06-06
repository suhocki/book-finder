package app.suhocki.mybooks.ui.filter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.domain.model.filter.*
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.listener.OnFilterAuthorClickListener
import app.suhocki.mybooks.ui.base.listener.OnFilterPublisherClickListener
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.filter.listener.*
import app.suhocki.mybooks.ui.search.SearchActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.AnkoContext
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
    OnSortNameToggleListener,
    OnSortPriceToggleListener {

    private val ui by lazy { FilterUI<FilterFragment>() }

    private val adapter by lazy {
        FilterAdapter(
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
        ui.apply.setOnClickListener {  }
        ui.reset.setOnClickListener {  }
    }

    override fun showFilterItems(
        filterItems: List<Any>,
        toggledCategoryPosition: Int
    ) {
        adapter.submitList(filterItems, onAnimationEnd = {
            if (toggledCategoryPosition != UNDEFINED_POSITION) {
                (ui.recyclerView.layoutManager as LinearLayoutManager)
                    .scrollToPositionWithOffset(toggledCategoryPosition, 0)
            }
        })
    }

    override fun onFilterCategoryClick(filterCategory: FilterCategory) {
        if (filterCategory.isExpanded) {
            presenter.collapseFilterCategory(filterCategory, adapter.items)
        } else presenter.expandFilterCategory(filterCategory, adapter.items)
    }

    override fun onFilterAuthorClick(filterAuthor: FilterAuthor) {

    }

    override fun onFilterYearClick(filterYear: FilterYear) {

    }

    override fun onFilterStatusClickClick(filterStatus: FilterStatus) {

    }

    override fun onFilterPublisherClick(filterPublisher: FilterPublisher) {

    }

    override fun onSortNameToggle(filterName: SortName) {
        val indexToToggle = adapter.items.indexOf(filterName)
        adapter.notifyItemChanged(indexToToggle)
    }

    override fun onSortPriceToggle(sortPrice: SortPrice) {
        val indexToToggle = adapter.items.indexOf(sortPrice)
        adapter.notifyItemChanged(indexToToggle)
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