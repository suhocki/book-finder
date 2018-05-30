package app.suhocki.mybooks.ui.catalog

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CatalogView : MvpView {

    fun showCatalogItems(catalogItems: List<Any>)

    fun showSearchView(expanded: Boolean)
}