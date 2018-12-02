package app.suhocki.mybooks.ui.catalog

import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.presentation.base.paginator.PaginationView
import app.suhocki.mybooks.ui.base.entity.UiBook
import app.suhocki.mybooks.ui.base.entity.UiItem
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CatalogView : MvpView, PaginationView<UiItem> {

    fun showSearchMode(expanded: Boolean)

    fun showBlankSearch()

    fun showRecyclerDecoration(decoration: RecyclerView.ItemDecoration)

    fun showTopRightButton(isClearTextMode: Boolean)

    fun showBuyDrawableForItem(book: UiBook, @DrawableRes drawableRes: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openBookWebsite(book: Book)
}