package app.suhocki.mybooks.di.provider.presentation

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.TypedItem
import app.suhocki.mybooks.presentation.categories.adapter.CategoryItemCallback
import javax.inject.Inject
import javax.inject.Provider

class CategoryItemCallbackProvider @Inject constructor() :
    Provider<CategoryItemCallback> {

    override fun get(): CategoryItemCallback = CategoryItemCallback().apply {
        set(object : DiffUtil.ItemCallback<TypedItem>() {
            override fun areItemsTheSame(oldItem: TypedItem, newItem: TypedItem): Boolean {
                return if (oldItem is Category && newItem is Category) oldItem.name == newItem.name
                else oldItem.type == newItem.type
            }

            override fun areContentsTheSame(oldItem: TypedItem, newItem: TypedItem): Boolean {
                return if (oldItem is Category && newItem is Category) oldItem.name == newItem.name
                else oldItem.type == newItem.type
            }
        })
    }
}