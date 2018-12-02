package app.suhocki.mybooks.presentation.base.paginator

interface State<T> {
    fun restart() {}
    fun refresh() {}
    fun loadNewPage() {}
    fun release() {}
    fun newData(data: List<T>) {}
    fun fail(error: Throwable) {}
}