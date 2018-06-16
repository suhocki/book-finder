package app.suhocki.mybooks.ui.catalog

import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.ui.base.entity.BookEntity
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CatalogView : MvpView {

    fun showCatalogItems(
        catalogItems: List<Any>,
        itemDecoration: RecyclerView.ItemDecoration? = null,
        scrollToPosition: Int = CatalogFragment.UNDEFINED_POSITION,
        updateSearchView: Boolean = false
    )

    fun showSearchMode(expanded: Boolean)

    fun showBlankSearch()

    fun showRecyclerDecoration(decoration: RecyclerView.ItemDecoration)

    fun showTopRightButton(isClearTextMode: Boolean)

    fun showBuyDrawableForItem(book: BookEntity, @DrawableRes drawableRes: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openBookWebsite(book: Book)
}