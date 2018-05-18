package app.suhocki.mybooks.di.provider.presentation

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.presentation.categories.adapter.CategoriesAdapter
import app.suhocki.mybooks.presentation.categories.adapter.CategoriesDiffer
import app.suhocki.mybooks.presentation.categories.adapter.CategoryItemCallback
import javax.inject.Inject
import javax.inject.Provider

class CategoriesDifferProvider @Inject constructor(
    private val adapter: CategoriesAdapter,
    private val itemCallback: CategoryItemCallback
): Provider<CategoriesDiffer> {

    override fun get(): CategoriesDiffer = CategoriesDiffer().apply {
        set(AsyncListDiffer<Category>(adapter, itemCallback.get()))
    }
}