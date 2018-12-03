package app.suhocki.mybooks.domain

import app.suhocki.mybooks.di.provider.CatalogRequestFactoryProvider
import app.suhocki.mybooks.ui.base.entity.PageProgress
import app.suhocki.mybooks.ui.base.entity.UiItem
import javax.inject.Inject

class ListTools @Inject constructor() {

    fun <T> updatePaginatedData(
        oldData: MutableList<T>,
        newPartOfData: List<T>,
        offset: Int,
        limit: Int
    ) {
        if (oldData.isEmpty()) {
            oldData.addAll(newPartOfData)
            return
        }

        val from =
            if (offset <= oldData.lastIndex) offset
            else oldData.size

        val to =
            if (offset + limit <= oldData.lastIndex) offset + limit
            else oldData.size

        oldData.removeAll(oldData.subList(from, to).toList())
        oldData.addAll(from, newPartOfData)
    }

    fun setNextPageTrigger(list: List<UiItem>) = with(list) {
        val nextPageTriggerPosition =
            if (size > CatalogRequestFactoryProvider.TRIGGER_OFFSET) size - CatalogRequestFactoryProvider.TRIGGER_OFFSET
            else lastIndex

        list.getOrNull(nextPageTriggerPosition)?.let { it.isNextPageTrigger = true }
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
}