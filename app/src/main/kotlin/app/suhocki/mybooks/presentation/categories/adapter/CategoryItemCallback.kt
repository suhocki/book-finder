package app.suhocki.mybooks.presentation.categories.adapter

import android.support.v7.util.DiffUtil
import app.suhocki.mybooks.domain.model.TypedItem
import javax.inject.Inject

class CategoryItemCallback @Inject constructor() {

    private lateinit var value: DiffUtil.ItemCallback<TypedItem>

    fun get(): DiffUtil.ItemCallback<TypedItem> {
        return value
    }

    fun set(value: DiffUtil.ItemCallback<TypedItem>) {
        this.value = value
    }
}