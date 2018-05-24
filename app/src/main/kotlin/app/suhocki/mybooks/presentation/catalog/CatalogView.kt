package app.suhocki.mybooks.presentation.catalog

import app.suhocki.mybooks.domain.model.CatalogItem
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface CatalogView : MvpView {

    fun showCatalogItems(categories: List<CatalogItem>)

    fun cancelAllNotifications()
}