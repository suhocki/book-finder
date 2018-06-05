package app.suhocki.mybooks.ui.base.search

interface OnSearchClickListener {

    fun onExpandSearchClick()

    fun onCollapseSearchClick(): Boolean

    fun onClearSearchClick()

    fun onStartSearchClick()
}