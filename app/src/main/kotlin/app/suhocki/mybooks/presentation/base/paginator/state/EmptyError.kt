package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.State
import app.suhocki.mybooks.presentation.base.paginator.PaginationView

class EmptyError<T> constructor(
    private val paginator: Paginator<T>,
    private val viewController: PaginationView<T>
) : State<T> {

    override fun restart() {
        paginator.currentState = EmptyProgress(paginator, viewController)
        viewController.showEmptyError(false)
        viewController.showEmptyProgress(true)
        paginator.loadPage()
    }

    override fun refresh() {
        paginator.currentState = EmptyProgress(paginator, viewController)
        viewController.showEmptyError(false)
        viewController.showEmptyProgress(true)
        paginator.loadPage()
    }

    override fun release() {
        paginator.currentState = Released()
        paginator.currentTask?.cancel(true)
    }
}