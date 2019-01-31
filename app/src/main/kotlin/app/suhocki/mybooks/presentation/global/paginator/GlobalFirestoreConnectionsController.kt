package app.suhocki.mybooks.presentation.global.paginator

class GlobalFirestoreConnectionsController {
    var eventReciever: ((count: Int) -> Unit)? = null

    fun onConnectionsCountChanged(count: Int) =
        eventReciever?.invoke(count)
}