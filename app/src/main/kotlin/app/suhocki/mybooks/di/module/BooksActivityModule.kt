package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.domain.model.Category
import toothpick.config.Module

class BooksActivityModule(category: Category) : Module() {
    init {
        bind(Category::class.java)
            .toInstance(category)
    }
}