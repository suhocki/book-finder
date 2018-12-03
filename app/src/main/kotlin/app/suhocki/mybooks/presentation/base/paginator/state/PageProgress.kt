package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.PaginationView
import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.State

class PageProgress<T> constructor(
    private val paginator: Paginator<T>,
    private val viewController: PaginationView<T>
) : State<T> {

    override fun restart() {
        paginator.toggleState<EmptyProgress<T>>()

        viewController.showData()
        viewController.hidePageProgress()
        viewController.showEmptyProgress(true)

        paginator.loadPage()
    }

    override fun newData(data: List<T>) {
        if (data.isNotEmpty()) {
            paginator.toggleState<Data<T>>()
            paginator.currentData.addAll(data)
            paginator.currentPage++

            viewController.hidePageProgress()
            viewController.showData(paginator.currentData)
        } else {
            paginator.toggleState<AllData<T>>()
            viewController.hidePageProgress()
        }
    }

    override fun refresh() {
        paginator.toggleState<Refresh<T>>()

        viewController.hidePageProgress()
        viewController.showRefreshProgress(true)

        paginator.loadPage()
    }

    override fun fail(error: Throwable) {
        paginator.toggleState<Data<T>>()

        viewController.hidePageProgress()
        viewController.showErrorMessage(error)
    }

    override fun release() {
        paginator.toggleState<Released<T>>()
        paginator.currentTask?.cancel(true)
    }
}