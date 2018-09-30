package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.data.assets.AssetsRepository
import app.suhocki.mybooks.domain.repository.LicenseRepository
import toothpick.config.Module

class LicensesModule : Module() {
    init {
        bind(LicenseRepository::class.java)
            .to(AssetsRepository::class.java)
            .singletonInScope()
    }
}