package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.PaginatorView
import app.suhocki.mybooks.presentation.base.paginator.State

class EmptyData<T> constructor(
    private val paginator: Paginator<T>,
    private val view: PaginatorView<T>
) : State<T> {

    override fun restart() {
        paginator.toggleState<EmptyProgress<T>>()
        view.showEmptyView(false)
        view.showEmptyProgress(true)
        paginator.loadPage()
    }

    override fun refresh() {
        paginator.toggleState<EmptyProgress<T>>()
        view.showEmptyView(false)
        view.showEmptyProgress(true)
        paginator.loadPage()
    }

    override fun release() {
        paginator.toggleState<Released<T>>()
        paginator.currentTask?.cancel(true)
    }
}