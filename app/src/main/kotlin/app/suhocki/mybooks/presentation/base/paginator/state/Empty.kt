package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.State
import app.suhocki.mybooks.presentation.base.paginator.PaginationView

class Empty<T> constructor(
    private val paginator: Paginator<T>,
    private val viewController: PaginationView<T>
) : State<T> {

    override fun refresh() {
        paginator.currentState = EmptyProgress(paginator, viewController)
        viewController.showEmptyProgress(true)
        paginator.loadPage()
    }

    override fun release() {
        paginator.currentState = Released()
        paginator.currentTask?.cancel(true)
    }
}