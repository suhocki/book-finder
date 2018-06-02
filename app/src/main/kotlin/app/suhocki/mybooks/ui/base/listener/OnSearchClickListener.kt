package app.suhocki.mybooks.ui.base.listener

interface OnSearchClickListener {

    fun onExpandSearchClick()

    fun onCollapseSearchClick(): Boolean

    fun onClearSearchClick()

    fun onStartSearchClick()
}