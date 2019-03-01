package app.suhocki.mybooks.model.system.debug

import app.suhocki.mybooks.data.firestore.entity.DebugInfo
import javax.inject.Inject

class DebugPanelController @Inject constructor() {
    private val data = listOf(
        DebugInfo.BANNER,
        DebugInfo.CATEGORIES,
        DebugInfo.BOOKS,
        DebugInfo.SHOP_INFO
    )

    var eventConsumer: ((Collection<DebugInfo>) -> Unit)? = null

    fun onBannerObserversCount(count: Int) {
        DebugInfo.BANNER.count = count
        notifyConsumer()
    }

    fun onCategoriesObserversCount(count: Int) {
        DebugInfo.CATEGORIES.count = count
        notifyConsumer()
    }

    fun onBooksObserversCount(count: Int) {
        DebugInfo.BOOKS.count = count
        notifyConsumer()
    }

    fun showCatalogObservers(show: Boolean) {
        DebugInfo.BANNER.visible = show
        DebugInfo.CATEGORIES.visible = show
        notifyConsumer()
    }

    fun showBooksObservers(show: Boolean) {
        DebugInfo.BOOKS.visible = show
        notifyConsumer()
    }

    private fun notifyConsumer() {
        val visibleData = data.filter { it.visible }
        eventConsumer?.invoke(visibleData)
    }
}