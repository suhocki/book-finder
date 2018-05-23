package app.suhocki.mybooks.presentation.categories

import app.suhocki.mybooks.domain.model.TypedItem
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface CategoriesView : MvpView {

    fun showCategories(categories: List<TypedItem>)

    fun cancelAllNotifications()
}