package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.Hint
import app.suhocki.mybooks.domain.model.Search
import toothpick.config.Module

class CatalogModule : Module() {
    init {
        bind(Search::class.java).toInstance(object : Search {
            override var searchQuery: String = EMPTY_STRING

            override val hintRes = R.string.hint_search
        })

        bind(Header::class.java).toInstance(object : Header {
            override var titleRes = R.string.catalog
        })

        bind(Hint::class.java).toInstance(object : Hint {
            override val hintRes = R.string.hint_search
        })
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}