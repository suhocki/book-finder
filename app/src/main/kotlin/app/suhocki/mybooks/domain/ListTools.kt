package app.suhocki.mybooks.domain

import app.suhocki.mybooks.presentation.base.paginator.Paginator
import app.suhocki.mybooks.ui.base.entity.PageProgress
import app.suhocki.mybooks.ui.base.entity.UiItem
import javax.inject.Inject

class ListTools @Inject constructor() {

    fun <T> updatePageData(
        allData: MutableList<T>,
        pageData: List<T>,
        offset: Int,
        limit: Int
    ) {
        if (allData.isEmpty()) {
            allData.addAll(pageData)
            return
        }

        val from =
            if (offset <= allData.lastIndex) offset
            else allData.size

        val to =
            if (offset + limit <= allData.lastIndex) offset + limit
            else allData.size

        allData.removeAll(allData.subList(from, to).toList())
        allData.addAll(from, pageData)
    }

    fun addProgressAndSetTrigger(list: MutableList<UiItem>, limit: Int) {
        list.removeAll { it is PageProgress }

        val isFullPage = list.isNotEmpty() && list.size.rem(limit) == 0

        if (isFullPage) {
            val triggerPosition =
                if (list.size > Paginator.TRIGGER_OFFSET) list.size - Paginator.TRIGGER_OFFSET
                else list.lastIndex

            list.getOrNull(triggerPosition)?.let { it.isNextPageTrigger = true }

            list.add(PageProgress())
        }
    }
}