package app.suhocki.mybooks.presentation.global

import android.support.annotation.IdRes

class GlobalMenuController {

    var drawerStateReceiver: ((open: Boolean) -> Unit)? = null
    val onAdminModeChangedReceivers: MutableList<() -> Unit> = mutableListOf()
    val onMenuItemSelectedListeners: MutableList<(menuItemId: Int) -> Unit> = mutableListOf()

    fun open() = drawerStateReceiver?.invoke(true)
    fun close() = drawerStateReceiver?.invoke(false)

    fun selectMenuItem(@IdRes menuItemId: Int) {
        onMenuItemSelectedListeners.forEach { it.invoke(menuItemId) }
    }

    fun notifyAdminModeChanged() {
        onAdminModeChangedReceivers.forEach { it.invoke() }
    }
}