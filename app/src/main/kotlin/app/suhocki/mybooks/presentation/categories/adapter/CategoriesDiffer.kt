package app.suhocki.mybooks.presentation.categories.adapter

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.domain.model.TypedItem
import javax.inject.Inject

class CategoriesDiffer @Inject constructor() {

    private lateinit var value: AsyncListDiffer<TypedItem>

    fun get(): AsyncListDiffer<TypedItem> {
        return value
    }

    fun set(value: AsyncListDiffer<TypedItem>) {
        this.value = value
    }
}