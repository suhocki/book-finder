package app.suhocki.mybooks

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.AppModule
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initToothpick()
        initAppScope()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private fun initToothpick() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
            FactoryRegistryLocator.setRootRegistry(app.suhocki.mybooks.FactoryRegistry())
            MemberInjectorRegistryLocator.setRootRegistry(app.suhocki.mybooks.MemberInjectorRegistry())
        }
    }

    private fun initAppScope() {
        val appScope = Toothpick.openScope(DI.APP_SCOPE)
        appScope.installModules(AppModule(this))
    }
}