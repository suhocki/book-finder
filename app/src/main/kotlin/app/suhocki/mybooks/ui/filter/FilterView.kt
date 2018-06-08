package app.suhocki.mybooks.ui.filter

import android.arch.persistence.db.SupportSQLiteQuery
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface FilterView : MvpView {
    fun showFilterItems(
        filterItems: List<Any>,
        toggledCategoryPosition: Int = FilterFragment.UNDEFINED_POSITION,
        needBottomButtonsUpdate: Boolean = false
    )

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showItem(filterItem: Any)

    fun showBottomButtonsVisible(configured: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showBooks(sqLiteQuery: SupportSQLiteQuery)
}