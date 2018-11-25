package app.suhocki.mybooks.ui.catalog

import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.presentation.base.Paginator
import app.suhocki.mybooks.ui.base.entity.UiBook
import app.suhocki.mybooks.ui.base.entity.UiItem
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CatalogView : MvpView, Paginator.ViewController<UiItem> {

    fun showCatalogItems(
        catalogItems: List<UiItem>,
        itemDecoration: RecyclerView.ItemDecoration? = null,
        scrollToPosition: Int = CatalogFragment.UNDEFINED_POSITION,
        updateSearchView: Boolean = false
    )

    fun showSearchMode(expanded: Boolean)

    fun showBlankSearch()

    fun showRecyclerDecoration(decoration: RecyclerView.ItemDecoration)

    fun showTopRightButton(isClearTextMode: Boolean)

    fun showBuyDrawableForItem(book: UiBook, @DrawableRes drawableRes: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openBookWebsite(book: Book)
}