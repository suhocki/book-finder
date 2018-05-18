package app.suhocki.mybooks.presentation.categories

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import app.suhocki.mybooks.domain.model.Category

@StateStrategyType(OneExecutionStateStrategy::class)
interface CategoriesView : MvpView {

    fun showCategories(categories: List<Category>)

    fun cancelAllNotifications()
}