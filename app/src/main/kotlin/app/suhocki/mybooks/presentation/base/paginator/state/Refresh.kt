package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.PaginatorView
import app.suhocki.mybooks.presentation.base.paginator.State

class Refresh<T> constructor(
    private val paginator: Paginator<T>,
    private val view: PaginatorView<T>
) : State<T> {

    override fun restart() {
        paginator.toggleState<EmptyProgress<T>>()
        view.showData()
        view.showRefreshProgress(false)
        view.showEmptyProgress(true)
        paginator.loadPage()
    }

    override fun newData(data: List<T>) {
        if (data.isNotEmpty()) {
            paginator.toggleState<Data<T>>()
            paginator.listOfT.clear()
            paginator.listOfT.addAll(data)
            paginator.currentPage = Paginator.FIRST_PAGE
            view.showRefreshProgress(false)
            view.showData(paginator.listOfT)
        } else {
            paginator.toggleState<EmptyData<T>>()
            paginator.listOfT.clear()
            view.showData()
            view.showRefreshProgress(false)
            view.showEmptyView(true)
        }
    }

    override fun fail(error: Throwable) {
        paginator.toggleState<Data<T>>()
        view.showRefreshProgress(false)
        view.showErrorMessage(error)
    }

    override fun release() {
        paginator.toggleState<Released<T>>()
        paginator.currentTask?.cancel(true)
    }
}