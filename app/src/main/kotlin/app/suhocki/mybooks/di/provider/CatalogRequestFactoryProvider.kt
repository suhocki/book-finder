package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.domain.ListTools
import app.suhocki.mybooks.presentation.base.paginator.Paginator.Companion.PAGE_SIZE
import app.suhocki.mybooks.ui.base.entity.UiItem
import javax.inject.Inject
import javax.inject.Provider

class CatalogRequestFactoryProvider @Inject constructor(
    private val firestoreObserver: FirestoreObserver,
    private val listTools: ListTools
) : Provider<(Int) -> List<UiItem>> {

    override fun get(): (Int) -> List<UiItem> = { page ->
        val pageData = firestoreObserver.observeCategories(
            page.dec() * PAGE_SIZE,
            PAGE_SIZE
        )
        listTools.addProgressAndSetTrigger(pageData, PAGE_SIZE)
        pageData
    }
}