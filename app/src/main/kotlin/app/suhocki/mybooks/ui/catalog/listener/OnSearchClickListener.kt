package app.suhocki.mybooks.ui.catalog.listener

interface OnSearchClickListener {

    fun onExpandSearchClick()

    fun onCollapseSearchClick(): Boolean

    fun onClearSearchClick()

    fun onStartSearchClick()
}