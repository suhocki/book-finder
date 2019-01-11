package app.suhocki.mybooks.ui.activity.listener

interface NavigationHandler {

    fun setDrawerExpanded(isExpanded: Boolean)

    fun setDrawerEnabled(isEnabled: Boolean)

    fun setBottomNavigationVisible(isVisible: Boolean)
}