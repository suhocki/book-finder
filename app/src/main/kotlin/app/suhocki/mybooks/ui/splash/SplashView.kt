package app.suhocki.mybooks.ui.splash

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface SplashView : MvpView {
    fun showMainScreen()

    fun showInitializationScreen()
}