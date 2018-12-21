package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.PaginatorView
import app.suhocki.mybooks.presentation.base.paginator.State

class PageProgress<T> constructor(
    private val paginator: Paginator<T>,
    private val view: PaginatorView<T>
) : State<T> {

    override fun restart() {
        paginator.toggleState<EmptyProgress<T>>()

        view.showData()
        view.hidePageProgress()
        view.showEmptyProgress(true)

        paginator.loadPage()
    }

    override fun newData(data: List<T>) {
        if (data.isNotEmpty()) {
            paginator.toggleState<Data<T>>()
            paginator.listOfT.addAll(data)

            view.showData(paginator.listOfT)
        } else {
            paginator.toggleState<AllData<T>>()
            view.hidePageProgress()
        }
    }

    override fun refresh() {
        paginator.toggleState<Refresh<T>>()

        view.hidePageProgress()
        view.showRefreshProgress(true)

        paginator.loadPage()
    }

    override fun fail(error: Throwable) {
        paginator.toggleState<Data<T>>()

        view.hidePageProgress()
        view.showErrorMessage(error)
    }

    override fun release() {
        paginator.toggleState<Released<T>>()
        paginator.currentTask?.cancel(true)
    }
}