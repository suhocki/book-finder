package app.suhocki.mybooks.ui.catalog

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CatalogView : MvpView {

    fun showCatalogItems(
        catalogItems: List<Any>,
        scrollToPosition: Int = CatalogFragment.UNDEFINED_POSITION
    )

    fun showSearchMode(expanded: Boolean)
}