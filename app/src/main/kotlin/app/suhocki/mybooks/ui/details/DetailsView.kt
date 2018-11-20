package app.suhocki.mybooks.ui.details

import android.support.annotation.DrawableRes
import app.suhocki.mybooks.domain.model.Book
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface DetailsView : MvpView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openBookWebsite(book: Book)

    fun showFabDrawableRes(@DrawableRes drawableRes: Int)

    fun showBook(book: Book)
}