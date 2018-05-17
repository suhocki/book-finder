package suhockii.dev.bookfinder.presentation.books

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import suhockii.dev.bookfinder.data.error.ErrorHandler
import suhockii.dev.bookfinder.data.error.ErrorListener
import suhockii.dev.bookfinder.data.error.ErrorType
import suhockii.dev.bookfinder.domain.BooksInteractor
import suhockii.dev.bookfinder.domain.model.Category
import javax.inject.Inject

@InjectViewState
class BooksPresenter @Inject constructor(
    private val interactor: BooksInteractor,
    private val errorHandler: ErrorHandler,
    private val category: Category
) : MvpPresenter<BooksView>(), ErrorListener {

    init {
        errorHandler.addListener(this)
    }

    override fun onError(error: ErrorType) =
        doAsync {
            uiThread {
                viewState.showProgressVisible(false)
            }
        }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            uiThread {
                viewState.showTitle(category.name)
                viewState.showProgressVisible(true)
            }
            interactor.getBooks(category)
                .let { books ->
                    uiThread {
                        if (books.isNotEmpty()) viewState.showBooks(books)
                        else viewState.showEmptyScreen()
                        viewState.showProgressVisible(false)
                    }
                }
        }
    }
}