package app.suhocki.mybooks.ui.details

import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.ui.base.entity.UiBook
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface DetailsView : MvpView {
    fun showBook(book: Book)
}