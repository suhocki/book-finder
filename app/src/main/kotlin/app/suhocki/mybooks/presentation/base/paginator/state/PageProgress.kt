package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.PaginationView
import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.State

class PageProgress<T> constructor(
    private val paginator: Paginator<T>,
    private val viewController: PaginationView<T>
) : State<T> {

    override fun restart() {
        paginator.currentState = EmptyProgress(paginator, viewController)

        viewController.showData()
        viewController.hidePageProgress()
        viewController.showEmptyProgress(true)

        paginator.loadPage()
    }

    override fun newData(data: List<T>) {
        if (data.isNotEmpty()) {
            paginator.currentState = Data(paginator, viewController)
            paginator.currentData.addAll(data)
            paginator.currentPage++

            viewController.hidePageProgress()
            viewController.showData(paginator.currentData)
        } else {
            paginator.currentState = AllData(paginator, viewController)
            viewController.hidePageProgress()
        }
    }

    override fun refresh() {
        paginator.currentState = Refresh(paginator, viewController)

        viewController.hidePageProgress()
        viewController.showRefreshProgress(true)

        paginator.loadPage()
    }

    override fun fail(error: Throwable) {
        paginator.currentState = Data(paginator, viewController)

        viewController.hidePageProgress()
        viewController.showErrorMessage(error)
    }

    override fun release() {
        paginator.currentState = Released()
        paginator.currentTask?.cancel(true)
    }
}