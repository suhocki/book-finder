package app.suhocki.mybooks.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.filter.*
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.search.OnSearchClickListener
import app.suhocki.mybooks.ui.filter.listener.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import toothpick.Toothpick

class FilterFragment : BaseFragment(), FilterView,
    OnFilterCategoryClickListener,
    OnFilterAuthorClickListener,
    OnFilterPublisherClickListener,
    OnFilterStatusClickListener,
    OnFilterYearClickListener,
    OnSearchClickListener {

    private val ui by lazy { FilterUI<FilterFragment>() }

    private val adapter by lazy {
        FilterAdapter(
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
    }

    override fun showFilterItems(filterItems: List<Any>) {
        adapter.submitList(filterItems)
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


    override fun onExpandSearchClick() {}

    override fun onCollapseSearchClick() = false

    override fun onClearSearchClick() {}

    override fun onStartSearchClick() {}

    companion object {
        fun newInstance() = FilterFragment()
    }
}