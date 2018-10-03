package app.suhocki.mybooks.ui.admin

import android.support.annotation.StringRes
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AdminView : MvpView {

    fun showData(
        data: List<Any>,
        changedPosition: Int = AdminAdapter.UNDEFINED,
        payload: Any? = null
    )

    fun showJsonProgress(isVisible: Boolean, progress: Int? = null)

    fun showError(@StringRes messageRes: Int? = null, isVisible: Boolean = true)
}