package suhockii.dev.bookfinder.di.module

import android.content.ServiceConnection
import suhockii.dev.bookfinder.di.provider.presentation.ServiceConnectionProvider
import suhockii.dev.bookfinder.presentation.initial.InitialUI
import toothpick.config.Module

class InitialActivityModule : Module() {
    init {
        bind(InitialUI::class.java).singletonInScope()
        bind(ServiceConnection::class.java).toProvider(ServiceConnectionProvider::class.java)
    }
}
