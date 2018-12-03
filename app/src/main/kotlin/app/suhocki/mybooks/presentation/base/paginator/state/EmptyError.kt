package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.State
import app.suhocki.mybooks.presentation.base.paginator.PaginationView

class EmptyError<T> constructor(
    private val paginator: Paginator<T>,
    private val viewController: PaginationView<T>
) : State<T> {

    override fun restart() {
        paginator.toggleState<EmptyProgress<T>>()
        viewController.showEmptyError(false)
        viewController.showEmptyProgress(true)
        paginator.loadPage()
    }

    override fun refresh() {
        paginator.toggleState<EmptyProgress<T>>()
        viewController.showEmptyError(false)
        viewController.showEmptyProgress(true)
        paginator.loadPage()
    }

    override fun release() {
        paginator.toggleState<Released<T>>()
        paginator.currentTask?.cancel(true)
    }
}