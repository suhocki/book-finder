package app.suhocki.mybooks.ui.books

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface BooksView : MvpView {

    fun showData(data: List<Any>)

    fun showEmptyProgress(show: Boolean)
    fun showEmptyError(show: Boolean, error: Throwable? = null)
    fun showEmptyView(show: Boolean)
    fun showErrorMessage(error: Throwable)
    fun showRefreshProgress(show: Boolean)

    fun showPageProgress(show: Boolean)

}