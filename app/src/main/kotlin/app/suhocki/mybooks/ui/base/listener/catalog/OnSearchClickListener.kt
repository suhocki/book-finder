package app.suhocki.mybooks.ui.base.listener.catalog

interface OnSearchClickListener {

    fun onExpandSearchClick()

    fun onCollapseSearchClick(): Boolean

    fun onClearSearchClick()

    fun onStartSearchClick()
}