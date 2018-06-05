package app.suhocki.mybooks.ui.filter

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface FilterView : MvpView {
    fun showFilterItems(
        filterItems: List<Any>,
        toggledCategoryPosition: Int = FilterFragment.UNDEFINED_POSITION
    )
}