package app.suhocki.mybooks.presentation.global

class GlobalMenuController {
    var drawerStateReceiver: ((open: Boolean) -> Unit)? = null

    fun open() = drawerStateReceiver?.invoke(true)
    fun close() = drawerStateReceiver?.invoke(false)
}