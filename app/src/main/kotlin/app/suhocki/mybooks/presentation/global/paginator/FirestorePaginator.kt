package app.suhocki.mybooks.presentation.global.paginator

import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.replaceInRange
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.catalog.CatalogPresenter
import com.google.firebase.firestore.DocumentSnapshot
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Future

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17,
 * changed by Maksim Sukhotski on 22.12.18.
 *
 * Changes:
 * - Implement applying firestore updates
 * - Replace rx.Disposable with java.util.concurrent.Future
 */
class FirestorePaginator<T : UiItem>(
    firestoreObserver: FirestoreObserver,
    private val requestFactory: (Int) -> List<T>,
    private val viewController: ViewController<T>,
    private val mapper: (List<DocumentSnapshot>) -> List<T>
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

    private val FIRST_PAGE = 1

    private var currentState: State<T> = EMPTY()
    private var currentPage = 0
    private val currentData = mutableListOf<T>()
    private var disposable: Future<Unit>? = null
    private val exceptionHandler = { throwable: Throwable ->
        currentState.fail(throwable)
    }

    init {
        with(firestoreObserver) {
            onPageChanged = {
                currentPage = it
            }

            onUpdate = { documentUpdates, offset, limit ->
                val categories = mapper.invoke(documentUpdates)

                when (documentUpdates.size) {
                    0 -> {
                        if (offset == 0) {
                            currentState = EMPTY_DATA()
                            currentData.clear()
                            viewController.showEmptyProgress(false)
                            viewController.showEmptyView(true)
                        } else {
                            currentState = ALL_DATA()
                            currentData.subList(offset, currentData.size).clear()
                            currentData.addAll(categories)
                        }
                    }

                    limit - offset -> {
                        currentState = DATA()
                        currentData.replaceInRange(categories, offset, limit)
                        currentData[currentData.lastIndex - CatalogPresenter.TRIGGER_OFFSET].isNextPageTrigger =
                                true
                        viewController.showPageProgress(true)
                    }

                    else -> {
                        currentState = ALL_DATA()
                        currentData.subList(offset, currentData.size).clear()
                        currentData.addAll(categories)
                    }
                }

                viewController.showData(true, currentData)
            }

            onWaitForNext = {
                currentData.forEach { it.isNextPageTrigger = false }
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

    private fun loadPage(page: Int) {
        disposable?.cancel(true)
        disposable = doAsync(exceptionHandler) {
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

    private inner class EMPTY : State<T> {

        override fun refresh() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.cancel(true)
        }
    }

    private inner class EMPTY_PROGRESS : State<T> {

        override fun restart() {
            loadPage(FIRST_PAGE)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentData.clear()
                currentData.addAll(data)
                currentPage = FIRST_PAGE
                viewController.showData(true, currentData)
                viewController.showEmptyProgress(false)
            } else {
                currentState = EMPTY_DATA()
                viewController.showEmptyProgress(false)
                viewController.showEmptyView(true)
            }
        }

        override fun fail(error: Throwable) {
            currentState = EMPTY_ERROR()
            viewController.showEmptyProgress(false)
            viewController.showEmptyError(true, error)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.cancel(true)
        }
    }

    private inner class EMPTY_ERROR : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyError(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyError(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.cancel(true)
        }
    }

    private inner class EMPTY_DATA : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyView(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyView(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.cancel(true)
        }
    }

    private inner class DATA : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showData(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.showRefreshProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun loadNewPage() {
            currentState = PAGE_PROGRESS()
            viewController.showPageProgress(true)
            loadPage(currentPage + 1)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.cancel(true)
        }
    }

    private inner class REFRESH : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showData(false)
            viewController.showRefreshProgress(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentData.clear()
                currentData.addAll(data)
                currentPage = FIRST_PAGE
                viewController.showRefreshProgress(false)
                viewController.showData(true, currentData)
            } else {
                currentState = EMPTY_DATA()
                currentData.clear()
                viewController.showData(false)
                viewController.showRefreshProgress(false)
                viewController.showEmptyView(true)
            }
        }

        override fun fail(error: Throwable) {
            currentState = DATA()
            viewController.showRefreshProgress(false)
            viewController.showErrorMessage(error)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.cancel(true)
        }
    }

    private inner class PAGE_PROGRESS : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showData(false)
            viewController.showPageProgress(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentData.addAll(data)
                currentPage++
                viewController.showPageProgress(false)
                viewController.showData(true, currentData)
            } else {
                currentState = ALL_DATA()
                viewController.showPageProgress(false)
            }
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.showPageProgress(false)
            viewController.showRefreshProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun fail(error: Throwable) {
            currentState = DATA()
            viewController.showPageProgress(false)
            viewController.showErrorMessage(error)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.cancel(true)
        }
    }

    private inner class ALL_DATA : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showData(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.showRefreshProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun release() {
            currentState = RELEASED()
            disposable?.cancel(true)
        }
    }

    private inner class RELEASED : State<T>
}
