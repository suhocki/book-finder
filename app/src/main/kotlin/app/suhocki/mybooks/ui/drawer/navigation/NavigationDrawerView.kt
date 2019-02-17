package app.suhocki.mybooks.ui.drawer.navigation

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface NavigationDrawerView : MvpView {
    fun showData(data: List<Any>)
    fun showAppSettings(adminEnabled: Boolean, debugEnabled: Boolean)
}