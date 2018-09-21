package app.suhocki.mybooks.ui.base.listener

interface AdminModeEnabler {
    fun toogleAdminMode(enabled: Boolean, withToast: Boolean = false)
}