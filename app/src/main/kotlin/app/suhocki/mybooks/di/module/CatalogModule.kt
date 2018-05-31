package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.Search
import toothpick.config.Module

class CatalogModule : Module() {
    init {
        bind(Search::class.java).toInstance(object : Search {
            override var searchQuery: String = EMPTY_STRING

            override val hintRes: Int
                get() = R.string.search
        })

        bind(Header::class.java).toInstance(object : Header {
            override val titleRes: Int
                get() = R.string.catalog
        })
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}