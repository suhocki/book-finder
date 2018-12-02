package app.suhocki.mybooks.presentation.base.paginator

interface PaginationView<T> {
    fun showEmptyProgress(show: Boolean)
    fun showEmptyError(show: Boolean, error: Throwable? = null)
    fun showEmptyView(show: Boolean)
    fun showData(data: List<T> = emptyList())
    fun showErrorMessage(error: Throwable)
    fun showRefreshProgress(show: Boolean)
    fun showPageProgress(visible: Boolean)
}