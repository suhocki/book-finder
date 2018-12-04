package app.suhocki.mybooks.domain

import app.suhocki.mybooks.di.provider.CatalogRequestFactoryProvider
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

    fun setNextPageTrigger(list: List<UiItem>) = with(list) {
        val triggerPosition =
            if (size > CatalogRequestFactoryProvider.TRIGGER_OFFSET) size - CatalogRequestFactoryProvider.TRIGGER_OFFSET
            else lastIndex

        list.getOrNull(triggerPosition)?.let { it.isNextPageTrigger = true }
    }

    fun addPageProgress(data: MutableList<UiItem>) {
        if (data.isNotEmpty() &&
            data.last() !is PageProgress
        ) {
            data.add(PageProgress())
        }
    }

    fun removePageProgress(data: MutableList<UiItem>) {
        data.removeAll { it is PageProgress }
    }


    fun findTriggerIndex(data: List<UiItem>) =
        data.find { it.isNextPageTrigger }?.let { data.indexOf(it) } ?: -1
}