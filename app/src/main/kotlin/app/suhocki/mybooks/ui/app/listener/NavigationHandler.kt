package app.suhocki.mybooks.ui.app.listener

interface NavigationHandler {

    fun setDrawerExpanded(isExpanded: Boolean)

    fun setDrawerEnabled(isEnabled: Boolean)

    fun setBottomNavigationVisible(isVisible: Boolean)
}