package app.suhocki.mybooks.ui.books

import app.suhocki.mybooks.domain.model.Category
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface BooksView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCategory(category: Category)

    fun showData(data: List<Any>)
    fun showEmptyProgress(show: Boolean)
    fun showEmptyError(show: Boolean, error: Throwable? = null)
    fun showEmptyView(show: Boolean)
    fun showErrorMessage(error: Throwable)
    fun showRefreshProgress(show: Boolean)
    fun showPageProgress(show: Boolean)

    fun showHamburgerMenu(animate: Boolean)

}