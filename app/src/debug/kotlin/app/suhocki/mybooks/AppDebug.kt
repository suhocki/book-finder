package app.suhocki.mybooks

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import com.facebook.stetho.Stetho
import toothpick.Toothpick
import com.squareup.leakcanary.LeakCanary
import toothpick.configuration.Configuration
import android.os.StrictMode


class AppDebug : App() {

    override fun onCreate() {
        initToothpick()
        super.onCreate()
        initLeakCanary()
        initStetho()
        enableStrictMode()
    }

    private fun initToothpick() {
        Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this);
    }

    private fun initLeakCanary() {
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this)
        }
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder().detectAll()
                .penaltyLog()
                .build()
        )
    }
}