package app.suhocki.mybooks.presentation.categories.adapter

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import app.suhocki.mybooks.domain.model.Category
import javax.inject.Inject

class CategoriesDiffer @Inject constructor() {

    private lateinit var value: AsyncListDiffer<Category>

    fun get(): AsyncListDiffer<Category> {
        return value
    }

    fun set(value: AsyncListDiffer<Category>) {
        this.value = value
    }
}