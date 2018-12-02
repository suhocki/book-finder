package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.PaginationView
import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.State

class EmptyProgress<T> constructor(
    private val paginator: Paginator<T>,
    private val viewController: PaginationView<T>
) : State<T> {

    override fun restart() {
        paginator.loadPage()
    }

    override fun newData(data: List<T>) {
        if (data.isNotEmpty()) {
            paginator.currentState = Data(paginator, viewController)
            paginator.currentData.clear()
            paginator.currentData.addAll(data)
            paginator.currentPage = Paginator.FIRST_PAGE
            viewController.showData(paginator.currentData)
            viewController.showEmptyProgress(false)
        } else {
            paginator.currentState = EmptyData(paginator, viewController)
            viewController.showEmptyProgress(false)
            viewController.showEmptyView(true)
        }
    }

    override fun fail(error: Throwable) {
        paginator.currentState = EmptyError(paginator, viewController)
        viewController.showEmptyProgress(false)
        viewController.showEmptyError(true, error)
    }

    override fun release() {
        paginator.currentState = Released()
        paginator.currentTask?.cancel(true)
    }
}