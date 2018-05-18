package app.suhocki.mybooks.presentation.details

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import app.suhocki.mybooks.domain.model.Book

@StateStrategyType(AddToEndSingleStrategy::class)
interface DetailsView : MvpView {

    fun showBookDetails(book: Book)
}