package app.suhocki.mybooks.ui.licenses

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface LicensesView : MvpView {

    fun showLicenses(data: List<Any>)
}