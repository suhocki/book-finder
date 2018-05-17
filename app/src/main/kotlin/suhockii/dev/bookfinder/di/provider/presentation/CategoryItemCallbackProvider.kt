package suhockii.dev.bookfinder.di.provider.presentation

import android.support.v7.util.DiffUtil
import suhockii.dev.bookfinder.domain.model.Category
import suhockii.dev.bookfinder.presentation.categories.adapter.CategoryItemCallback
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