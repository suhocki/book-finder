package app.suhocki.mybooks.presentation.base.paginator

import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.di.CatalogRequestFactory
import app.suhocki.mybooks.presentation.base.paginator.state.*
import app.suhocki.mybooks.uiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Future
import javax.inject.Inject


class Paginator<T> @Inject constructor(
    val viewController: PaginationView<T>,
    val currentData: MutableList<T>,
    @CatalogRequestFactory private val requestFactory: (Int) -> List<T>,
    firestoreObserver: FirestoreObserver
) {

    private var currentState: State<T> = Empty(this, viewController)
    internal var currentPage = FIRST_PAGE
    internal var currentTask: Future<Unit>? = null

    init {
        with(firestoreObserver) {
            onEmptyData = {
                currentPage = FIRST_PAGE
                toggleState<EmptyData<T>>()
                viewController.showEmptyProgress(false)
                viewController.showEmptyView(true)
            }
            onNotEmptyData = {
                if (currentState !is Data) {
                    toggleState<Data<T>>()
                }
            }
            onCurrentPageChanged = {
                currentPage = it
            }
        }
    }

    fun restart() {
        currentState.restart()
    }

    fun refresh() {
        currentState.refresh()
    }

    fun loadNewPage() {
        currentState.loadNewPage()
    }

    fun release() {
        currentState.release()
    }

    internal fun loadPage(page: Int = FIRST_PAGE) {
        doAsync({ throwable -> uiThread { currentState.fail(throwable) } }) {
            val data = requestFactory.invoke(page)
            uiThread {
                currentState.newData(data)
            }
        }.apply {
            currentTask = this
        }
    }

    internal inline fun <reified T> toggleState() {
        currentState = when (T::class.java) {
            AllData::class.java -> AllData(this, viewController)
            Data::class.java -> Data(this, viewController)
            Empty::class.java -> Empty(this, viewController)
            EmptyData::class.java -> EmptyData(this, viewController)
            EmptyError::class.java -> EmptyError(this, viewController)
            EmptyProgress::class.java -> EmptyProgress(this, viewController)
            PageProgress::class.java -> PageProgress(this, viewController)
            Refresh::class.java -> Refresh(this, viewController)
            Released::class.java -> Released()
            else -> throw NotImplementedError("Cannot determine state: ${T::class.java}")
        }
    }

    companion object {
        const val FIRST_PAGE = 1
        const val PAGE_SIZE = 20
        const val TRIGGER_OFFSET = 4
    }
}