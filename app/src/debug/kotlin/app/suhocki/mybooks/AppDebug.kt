package app.suhocki.mybooks

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import toothpick.Toothpick
import com.squareup.leakcanary.LeakCanary
import toothpick.configuration.Configuration


class AppDebug : App() {

    override fun onCreate() {
        initToothpick()
        super.onCreate()
        initLeakCanary()
    }

    private fun initToothpick() {
        Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
    }

    private fun initLeakCanary() {
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }
    }
}