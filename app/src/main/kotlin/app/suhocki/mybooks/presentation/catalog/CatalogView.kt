package app.suhocki.mybooks.presentation.catalog

import app.suhocki.mybooks.domain.model.CatalogItem
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CatalogView : MvpView {

    fun showCatalogItems(catalogItems: List<CatalogItem>)
}