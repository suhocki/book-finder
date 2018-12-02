package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.PaginationView
import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.State

class Refresh<T> constructor(
    private val paginator: Paginator<T>,
    private val viewController: PaginationView<T>
) : State<T> {

    override fun restart() {
        paginator.currentState = EmptyProgress(paginator, viewController)
        viewController.showData()
        viewController.showRefreshProgress(false)
        viewController.showEmptyProgress(true)
        paginator.loadPage()
    }

    override fun newData(data: List<T>) {
        if (data.isNotEmpty()) {
            paginator.currentState = Data(paginator, viewController)
            paginator.currentData.clear()
            paginator.currentData.addAll(data)
            paginator.currentPage = Paginator.FIRST_PAGE
            viewController.showRefreshProgress(false)
            viewController.showData(paginator.currentData)
        } else {
            paginator.currentState = EmptyData(paginator, viewController)
            paginator.currentData.clear()
            viewController.showData()
            viewController.showRefreshProgress(false)
            viewController.showEmptyView(true)
        }
    }

    override fun fail(error: Throwable) {
        paginator.currentState = Data(paginator, viewController)
        viewController.showRefreshProgress(false)
        viewController.showErrorMessage(error)
    }

    override fun release() {
        paginator.currentState = Released()
        paginator.currentTask?.cancel(true)
    }
}