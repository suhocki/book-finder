package app.suhocki.mybooks.presentation.base.paginator.state

import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.presentation.base.paginator.PaginatorView
import app.suhocki.mybooks.presentation.base.paginator.State

class EmptyProgress<T> constructor(
    private val paginator: Paginator<T>,
    private val view: PaginatorView<T>
) : State<T> {

    override fun restart() {
        paginator.loadPage()
    }

    override fun newData(data: List<T>) {
        if (data.isNotEmpty()) {
            paginator.toggleState<Data<T>>()
            paginator.listOfT.clear()
            paginator.listOfT.addAll(data)
            paginator.currentPage = Paginator.FIRST_PAGE
            view.showData(paginator.listOfT)
            view.showEmptyProgress(false)
        } else {
            paginator.toggleState<EmptyData<T>>()
            view.showEmptyProgress(false)
            view.showEmptyView(true)
        }
    }

    override fun fail(error: Throwable) {
        paginator.toggleState<EmptyError<T>>()
        view.showEmptyProgress(false)
        view.showEmptyError(true, error)
    }

    override fun release() {
        paginator.toggleState<Released<T>>()
        paginator.currentTask?.cancel(true)
    }
}