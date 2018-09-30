package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.data.assets.AssetsRepository
import app.suhocki.mybooks.domain.repository.ChangelogRepository
import toothpick.config.Module

class ChangelogModule : Module() {
    init {
        bind(ChangelogRepository::class.java)
            .to(AssetsRepository::class.java)
            .singletonInScope()
    }
}