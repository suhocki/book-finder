package app.suhocki.mybooks.ui.search

import android.os.Parcelable
import android.support.annotation.StringRes
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SearchView : MvpView {
    fun showTitleRes(@StringRes title: Int)

    fun showSearchItems(searchItems: List<Any>)

    fun showProgressBar(isVisible: Boolean)

    fun showEmptyScreen()

    fun finishWithResult(searchKey: String, parcelable: Parcelable)
}