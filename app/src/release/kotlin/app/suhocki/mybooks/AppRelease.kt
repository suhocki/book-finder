package app.suhocki.mybooks

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator


class AppRelease : App() {

    override fun onCreate() {
        initFabric()
        initToothpick()
        super.onCreate()
    }

    private fun initFabric() {
        Fabric.with(this, Crashlytics())
    }

    private fun initToothpick() {
        Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
        FactoryRegistryLocator.setRootRegistry(app.suhocki.mybooks.FactoryRegistry())
        MemberInjectorRegistryLocator.setRootRegistry(app.suhocki.mybooks.MemberInjectorRegistry())
    }
}