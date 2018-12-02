package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.PaginationView
import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.State

class AllData<T> constructor(
    private val paginator: Paginator<T>,
    private val viewController: PaginationView<T>
) : State<T> {

    override fun restart() {
        paginator.currentState = EmptyProgress(paginator, viewController)
        viewController.showData()
        viewController.showEmptyProgress(true)
        paginator.loadPage()
    }

    override fun refresh() {
        paginator.currentState = Refresh(paginator, viewController)
        viewController.showRefreshProgress(true)
        paginator.loadPage()
    }

    override fun release() {
        paginator.currentState = Released()
        paginator.currentTask?.cancel(true)
    }
}