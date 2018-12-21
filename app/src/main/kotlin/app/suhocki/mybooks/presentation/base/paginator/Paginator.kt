package app.suhocki.mybooks.presentation.base.paginator

import app.suhocki.mybooks.presentation.base.paginator.state.*
import app.suhocki.mybooks.uiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Future


class Paginator<T> constructor(
    val view: PaginatorView<T>,
    val listOfT: MutableList<T>,
    private val requestFactory: (Int) -> List<T>
) {

    var currentState: State<T> = Empty(this, view)

    internal var currentPage = FIRST_PAGE
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
            uiThread {
                currentState.newData(data)
            }
        }.apply {
            currentTask = this
        }
    }

    internal inline fun <reified T> toggleState() {
        currentState = when (T::class.java) {
            AllData::class.java -> AllData(this, view)
            Data::class.java -> Data(this, view)
            Empty::class.java -> Empty(this, view)
            EmptyData::class.java -> EmptyData(this, view)
            EmptyError::class.java -> EmptyError(this, view)
            EmptyProgress::class.java -> EmptyProgress(this, view)
            PageProgress::class.java -> PageProgress(this, view)
            Refresh::class.java -> Refresh(this, view)
            Released::class.java -> Released()
            else -> throw NotImplementedError("Cannot determine state: ${T::class.java}")
        }
    }

    companion object {
        const val FIRST_PAGE = 1
        const val LIMIT = 3
    }
}