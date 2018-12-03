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
            paginator.toggleState<Data<T>>()
            paginator.currentData.clear()
            paginator.currentData.addAll(data)
            paginator.currentPage = Paginator.FIRST_PAGE
            viewController.showData(paginator.currentData)
            viewController.showEmptyProgress(false)
        } else {
            paginator.toggleState<EmptyData<T>>()
            viewController.showEmptyProgress(false)
            viewController.showEmptyView(true)
        }
    }

    override fun fail(error: Throwable) {
        paginator.toggleState<EmptyError<T>>()
        viewController.showEmptyProgress(false)
        viewController.showEmptyError(true, error)
    }

    override fun release() {
        paginator.toggleState<Released<T>>()
        paginator.currentTask?.cancel(true)
    }
}