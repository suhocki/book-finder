package app.suhocki.mybooks.presentation.global

import app.suhocki.mybooks.uiThread

class GlobalFirestoreConnectionsController {
    var eventReciever: ((count: Int) -> Unit)? = null

    fun onConnectionsCountChanged(count: Int) =
        uiThread { eventReciever?.invoke(count) }
}