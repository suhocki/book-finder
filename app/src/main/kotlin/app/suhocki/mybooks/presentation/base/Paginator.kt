package app.suhocki.mybooks.presentation.base

import app.suhocki.mybooks.di.RequestFactory
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Future
import javax.inject.Inject


class Paginator<T> @Inject constructor(
    @RequestFactory private val requestFactory: (Int) -> List<T>,
    private val viewController: ViewController<T>
) {

    interface ViewController<T> {
        fun showEmptyProgress(show: Boolean)
        fun showEmptyError(show: Boolean, error: Throwable? = null)
        fun showEmptyView(show: Boolean)
        fun showData(show: Boolean, data: List<T> = emptyList())
        fun showErrorMessage(error: Throwable)
        fun showRefreshProgress(show: Boolean)
        fun showPageProgress(show: Boolean)
    }

    private var currentState: State<T> = Empty()
    private var currentPage = 0
    private val currentData = mutableListOf<T>()
    private var currentTask: Future<Unit>? = null

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

    private fun loadPage(page: Int = 1) {
        currentTask = doAsync({
            currentState.fail(it)
        }) {
            val data = requestFactory.invoke(page)
            uiThread { currentState.newData(data) }
        }
    }

    private interface State<T> {
        fun restart() {}
        fun refresh() {}
        fun loadNewPage() {}
        fun release() {}
        fun newData(data: List<T>) {}
        fun fail(error: Throwable) {}
    }

    private inner class Empty : State<T> {

        override fun refresh() {
            currentState = EmptyProgress()
            viewController.showEmptyProgress(true)
            loadPage()
        }

        override fun release() {
            currentState = RELEASED()
            currentTask?.cancel(true)
        }
    }

    private inner class EmptyProgress : State<T> {

        override fun restart() {
            loadPage()
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = Data()
                currentData.clear()
                currentData.addAll(data)
                currentPage = 1
                viewController.showData(true, currentData)
                viewController.showEmptyProgress(false)
            } else {
                currentState = EmptyData()
                viewController.showEmptyProgress(false)
                viewController.showEmptyView(true)
            }
        }

        override fun fail(error: Throwable) {
            currentState = EmptyError()
            viewController.showEmptyProgress(false)
            viewController.showEmptyError(true, error)
        }

        override fun release() {
            currentState = RELEASED()
            currentTask?.cancel(true)
        }
    }

    private inner class EmptyError : State<T> {

        override fun restart() {
            currentState = EmptyProgress()
            viewController.showEmptyError(false)
            viewController.showEmptyProgress(true)
            loadPage()
        }

        override fun refresh() {
            currentState = EmptyProgress()
            viewController.showEmptyError(false)
            viewController.showEmptyProgress(true)
            loadPage()
        }

        override fun release() {
            currentState = RELEASED()
            currentTask?.cancel(true)
        }
    }

    private inner class EmptyData : State<T> {

        override fun restart() {
            currentState = EmptyProgress()
            viewController.showEmptyView(false)
            viewController.showEmptyProgress(true)
            loadPage()
        }

        override fun refresh() {
            currentState = EmptyProgress()
            viewController.showEmptyView(false)
            viewController.showEmptyProgress(true)
            loadPage()
        }

        override fun release() {
            currentState = RELEASED()
            currentTask?.cancel(true)
        }
    }

    private inner class Data : State<T> {

        override fun restart() {
            currentState = EmptyProgress()
            viewController.showData(false)
            viewController.showEmptyProgress(true)
            loadPage()
        }

        override fun refresh() {
            currentState = Refresh()
            viewController.showRefreshProgress(true)
            loadPage()
        }

        override fun loadNewPage() {
            currentState = PageProgress()
            viewController.showPageProgress(true)
            loadPage(currentPage + 1)
        }

        override fun release() {
            currentState = RELEASED()
            currentTask?.cancel(true)
        }
    }

    private inner class Refresh : State<T> {

        override fun restart() {
            currentState = EmptyProgress()
            viewController.showData(false)
            viewController.showRefreshProgress(false)
            viewController.showEmptyProgress(true)
            loadPage()
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = Data()
                currentData.clear()
                currentData.addAll(data)
                currentPage = 1
                viewController.showRefreshProgress(false)
                viewController.showData(true, currentData)
            } else {
                currentState = EmptyData()
                currentData.clear()
                viewController.showData(false)
                viewController.showRefreshProgress(false)
                viewController.showEmptyView(true)
            }
        }

        override fun fail(error: Throwable) {
            currentState = Data()
            viewController.showRefreshProgress(false)
            viewController.showErrorMessage(error)
        }

        override fun release() {
            currentState = RELEASED()
            currentTask?.cancel(true)
        }
    }

    private inner class PageProgress : State<T> {

        override fun restart() {
            currentState = EmptyProgress()
            viewController.showData(false)
            viewController.showPageProgress(false)
            viewController.showEmptyProgress(true)
            loadPage()
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = Data()
                currentData.addAll(data)
                currentPage++
                viewController.showPageProgress(false)
                viewController.showData(true, currentData)
            } else {
                currentState = AllData()
                viewController.showPageProgress(false)
            }
        }

        override fun refresh() {
            currentState = Refresh()
            viewController.showPageProgress(false)
            viewController.showRefreshProgress(true)
            loadPage()
        }

        override fun fail(error: Throwable) {
            currentState = Data()
            viewController.showPageProgress(false)
            viewController.showErrorMessage(error)
        }

        override fun release() {
            currentState = RELEASED()
            currentTask?.cancel(true)
        }
    }

    private inner class AllData : State<T> {

        override fun restart() {
            currentState = EmptyProgress()
            viewController.showData(false)
            viewController.showEmptyProgress(true)
            loadPage()
        }

        override fun refresh() {
            currentState = Refresh()
            viewController.showRefreshProgress(true)
            loadPage()
        }

        override fun release() {
            currentState = RELEASED()
            currentTask?.cancel(true)
        }
    }

    private inner class RELEASED : State<T>
}