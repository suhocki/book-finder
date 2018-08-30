package app.suhocki.mybooks.ui.changelog

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChangelogView : MvpView {

    fun showChangelog(data: List<Any>)
}