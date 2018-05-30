package app.suhocki.mybooks.ui.base.listener

interface NavigationHandler {

    fun setDrawerExpanded(isExpanded: Boolean)

    fun setDrawerEnabled(isEnabled: Boolean)

    fun setBottomNavigationVisible(isVisible: Boolean)
}