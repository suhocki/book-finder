package app.suhocki.mybooks

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.AppModule
import toothpick.Toothpick


open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initAppScope()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private fun initAppScope() {
        val appScope = Toothpick.openScope(DI.APP_SCOPE)
        appScope.installModules(AppModule(this))
    }
}