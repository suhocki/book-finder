package app.suhocki.mybooks.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.ui.base.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import toothpick.Toothpick

class FilterFragment : BaseFragment(), FilterView {

    private val ui by lazy { FilterUI<FilterFragment>() }

    private val adapter by lazy { FilterAdapter() }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ui.recyclerView.adapter = adapter
    }

    override fun showFilterItems(filterItems: List<Any>) {
        adapter.submitList(filterItems)
    }

    companion object {
        fun newInstance() = FilterFragment()
    }
}