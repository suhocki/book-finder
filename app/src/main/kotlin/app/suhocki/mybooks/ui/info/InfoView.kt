package app.suhocki.mybooks.ui.info

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface InfoView : MvpView {
    fun showInfoItems(items: MutableList<Any>)
}