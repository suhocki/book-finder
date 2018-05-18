package app.suhocki.mybooks.di.module

import android.content.ServiceConnection
import app.suhocki.mybooks.di.provider.presentation.ServiceConnectionProvider
import app.suhocki.mybooks.presentation.initial.InitialUI
import toothpick.config.Module

class InitialActivityModule : Module() {
    init {
        bind(InitialUI::class.java).singletonInScope()
        bind(ServiceConnection::class.java).toProvider(ServiceConnectionProvider::class.java)
    }
}
