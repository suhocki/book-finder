package app.suhocki.mybooks.ui.books

import android.support.annotation.DrawableRes
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.ui.base.entity.BookEntity
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface BooksView : MvpView {

    fun showTitle(title: String)

    fun showBooks(books: List<Book>, scrollToTop: Boolean = false)

    fun showEmptyScreen(isEmpty: Boolean)

    fun showProgressVisible(visible: Boolean)

    fun showDrawerExpanded(isExpanded: Boolean)

    fun showBuyDrawableForItem(book: BookEntity, @DrawableRes drawableRes: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openBookWebsite(book: Book)
}