package app.suhocki.mybooks.ui.search

import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.firestore.entity.FirestoreBook
import app.suhocki.mybooks.domain.model.BookSearchResult
import app.suhocki.mybooks.model.system.flow.FlowRouter
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SearchPresenter @Inject constructor(
    private val errorHandler: ErrorHandler,
    private val flowRouter: FlowRouter
) : MvpPresenter<SearchView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val book = FirestoreBook()
        viewState.showData(
            listOf(
                BookSearchResult("",book),
                BookSearchResult("",book),
                BookSearchResult("",book),
                BookSearchResult("",book),
                BookSearchResult("",book),
                BookSearchResult("",book),
                BookSearchResult("",book),
                BookSearchResult("",book),
                BookSearchResult("",book),
                BookSearchResult("",book),
                BookSearchResult("",book),
                BookSearchResult("",book)
            )
        )
    }

    fun onBackPressed() = flowRouter.exit()
}