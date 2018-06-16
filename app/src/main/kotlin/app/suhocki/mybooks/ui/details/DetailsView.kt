package app.suhocki.mybooks.ui.details

import android.support.annotation.DrawableRes
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface DetailsView : MvpView {

    fun openBookWebsite()

    fun showFabDrawableRes(@DrawableRes drawableRes: Int)
}