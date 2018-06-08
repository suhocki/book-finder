package app.suhocki.mybooks.ui.base.listener

import android.arch.persistence.db.SupportSQLiteQuery

interface OnFilterResultListener {

    fun onFilterResult(searchQuery: SupportSQLiteQuery)

    fun onFilterReset()
}
