package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.PaginationView
import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.State

class Refresh<T> constructor(
    private val paginator: Paginator<T>,
    private val viewController: PaginationView<T>
) : State<T> {

    override fun restart() {
        paginator.toggleState<EmptyProgress<T>>()
        viewController.showData()
        viewController.showRefreshProgress(false)
        viewController.showEmptyProgress(true)
        paginator.loadPage()
    }

    override fun newData(data: List<T>) {
        if (data.isNotEmpty()) {
            paginator.toggleState<Data<T>>()
            paginator.currentData.clear()
            paginator.currentData.addAll(data)
            paginator.currentPage = Paginator.FIRST_PAGE
            viewController.showRefreshProgress(false)
            viewController.showData(paginator.currentData)
        } else {
            paginator.toggleState<EmptyData<T>>()
            paginator.currentData.clear()
            viewController.showData()
            viewController.showRefreshProgress(false)
            viewController.showEmptyView(true)
        }
    }

    override fun fail(error: Throwable) {
        paginator.toggleState<Data<T>>()
        viewController.showRefreshProgress(false)
        viewController.showErrorMessage(error)
    }

    override fun release() {
        paginator.toggleState<Released<T>>()
        paginator.currentTask?.cancel(true)
    }
}