package app.suhocki.mybooks.ui.main.listener

interface NavigationHandler {

    fun setDrawerExpanded(isExpanded: Boolean)

    fun setDrawerEnabled(isEnabled: Boolean)

    fun setBottomNavigationVisible(isVisible: Boolean)
}