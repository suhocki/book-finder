package app.suhocki.mybooks.di.provider.presentation

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.presentation.categories.adapter.CategoryItemCallback
import javax.inject.Inject
import javax.inject.Provider

class CategoryItemCallbackProvider @Inject constructor() :
    Provider<CategoryItemCallback> {

    override fun get(): CategoryItemCallback = CategoryItemCallback().apply {
        set(object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.name == newItem.name
            }
        })
    }
}