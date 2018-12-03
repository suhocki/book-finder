package app.suhocki.mybooks.presentation.base.paginator

import app.suhocki.mybooks.di.CatalogRequestFactory
import app.suhocki.mybooks.presentation.base.paginator.state.*
import app.suhocki.mybooks.uiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Future
import javax.inject.Inject


class Paginator<T> @Inject constructor(
    private val viewController: PaginationView<T>,
    val currentData: MutableList<T>,
    @CatalogRequestFactory private val requestFactory: (Int) -> List<T>
) {

    internal var state: State<T> = Empty(this, viewController)
    internal var currentPage = 0
    internal var currentTask: Future<Unit>? = null

    fun restart() {
        state.restart()
    }

    fun refresh() {
        state.refresh()
    }

    fun loadNewPage() {
        state.loadNewPage()
    }

    fun release() {
        state.release()
    }

    internal fun loadPage(page: Int = FIRST_PAGE) {
        doAsync({ throwable -> uiThread { state.fail(throwable) } }) {
            val data = requestFactory.invoke(page)

            this.uiThread {
                state.newData(data)
            }
        }.apply {
            currentTask = this
        }
    }

    internal inline fun <reified T> toggleState() {
        state = when (T::class.java) {
            AllData::class.java -> AllData(this, viewController)
            Data::class.java -> Data(this, viewController)
            Empty::class.java -> Empty(this, viewController)
            EmptyError::class.java -> EmptyError(this, viewController)
            EmptyProgress::class.java -> EmptyProgress(this, viewController)
            PageProgress::class.java -> PageProgress(this, viewController)
            Refresh::class.java -> Refresh(this, viewController)
            Released::class.java -> Released()
            else -> throw NotImplementedError("Cannot determine paginator state")
        }
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}