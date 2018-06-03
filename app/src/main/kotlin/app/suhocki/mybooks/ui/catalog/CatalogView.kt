package app.suhocki.mybooks.ui.catalog

import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CatalogView : MvpView {

    fun showCatalogItems(
        catalogItems: List<Any>,
        itemDecoration: RecyclerView.ItemDecoration? = null,
        scrollToPosition: Int = CatalogFragment.UNDEFINED_POSITION,
        updateSearchView: Boolean = false
    )

    fun showSearchMode(expanded: Boolean)

    fun showBlankSearch()

    fun showRecyclerDecoration(decoration: RecyclerView.ItemDecoration)
}