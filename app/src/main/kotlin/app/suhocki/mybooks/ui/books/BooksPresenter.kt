package app.suhocki.mybooks.ui.books

import app.suhocki.mybooks.Screens
import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.firestore.entity.FirestoreCategory
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.model.system.flow.FlowRouter
import app.suhocki.mybooks.presentation.global.paginator.FirestorePaginator
import app.suhocki.mybooks.ui.base.entity.UiBook
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.firebase.firestore.Query
import javax.inject.Inject

@InjectViewState
class BooksPresenter @Inject constructor(
    private val firestoreObserver: FirestoreObserver,
    private val categoryQuery: Query,
    private val mapper: Mapper,
    private val router: FlowRouter
) : MvpPresenter<BooksView>() {

    private val booksFactory: (Int) -> List<UiBook> = { page: Int ->
        val snapshot = firestoreObserver.observePage(
            page.dec() * LIMIT,
            LIMIT
        )
        val pageData = snapshot
            .mapTo(mutableListOf()) { mapper.map<UiBook>(it) }
        pageData
    }

    private val paginatorView = object : FirestorePaginator.ViewController<Any> {
        override fun showEmptyProgress(show: Boolean) {
            viewState.showEmptyProgress(show)
        }

        override fun showEmptyError(show: Boolean, error: Throwable?) {
            error?.let {
                viewState.showEmptyError(show, error)
            } ?: viewState.showEmptyError(show, null)
        }

        override fun showErrorMessage(error: Throwable) {
            viewState.showErrorMessage(error)
        }

        override fun showEmptyView(show: Boolean) {
            viewState.showEmptyView(show)
        }

        override fun showData(show: Boolean, data: List<Any>) {
            viewState.showData(data)
        }

        override fun showRefreshProgress(show: Boolean) {
            viewState.showRefreshProgress(show)
        }

        override fun showPageProgress(show: Boolean) {
            viewState.showPageProgress(show)
        }
    }

    private val paginator = FirestorePaginator(
        firestoreObserver,
        booksFactory,
        paginatorView,
        { firestoreData -> firestoreData.map { mapper.map<UiBook>(it) } }
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        paginator.refresh()
        observeCategory()
    }

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
        firestoreObserver.dispose()
    }

    fun loadNextPage() {
        paginator.loadNewPage()
    }

    fun onBookClick(book: Book) {
        router.navigateTo(Screens.Details(book.id))
    }

    fun onBackPressed() = router.exit()

    private fun observeCategory() {
        categoryQuery.addSnapshotListener { snapshot, error ->
            if (snapshot == null) {
                viewState.showErrorMessage(error!!)
                return@addSnapshotListener
            }
            snapshot.documents
                .firstOrNull()
                ?.let {
                    viewState.showCategory(it.toObject(FirestoreCategory::class.java)!!)
                }
        }
    }

    companion object {
        const val LIMIT = 3
    }
}