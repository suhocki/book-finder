package app.suhocki.mybooks.presentation.base.paginator

import app.suhocki.mybooks.di.CatalogRequestFactory
import app.suhocki.mybooks.presentation.base.paginator.state.Empty
import app.suhocki.mybooks.uiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Future
import javax.inject.Inject


class Paginator<T> @Inject constructor(
    viewController: PaginationView<T>,
    val currentData: MutableList<T>,
    @CatalogRequestFactory private val requestFactory: (Int) -> List<T>
) {

    internal var currentState: State<T> = Empty(this, viewController)
    internal var currentPage = 0
    internal var currentTask: Future<Unit>? = null

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

            this.uiThread {
                currentState.newData(data)
            }
        }.apply {
            currentTask = this
        }
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}