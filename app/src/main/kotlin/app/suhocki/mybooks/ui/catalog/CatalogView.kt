package app.suhocki.mybooks.ui.catalog

import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.ui.base.entity.UiBook
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface CatalogView : MvpView {
    fun showData(data: List<Any>)
    fun showEmptyProgress(show: Boolean)
    fun showEmptyError(show: Boolean, error: Throwable? = null)
    fun showEmptyView(show: Boolean)
    fun showErrorMessage(error: Throwable)
    fun showRefreshProgress(show: Boolean)

    fun showRecyclerDecoration(decoration: RecyclerView.ItemDecoration)
    fun showBuyDrawableForItem(book: UiBook, @DrawableRes drawableRes: Int)
    fun openBookWebsite(book: Book)
}