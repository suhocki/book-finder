package app.suhocki.mybooks.presentation.books

import app.suhocki.mybooks.domain.model.Book
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface BooksView : MvpView {

    fun showTitle(title: String)

    fun showBooks(books: List<Book>)

    fun showEmptyScreen()

    fun showProgressVisible(visible: Boolean)
}