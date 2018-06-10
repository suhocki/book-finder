package app.suhocki.mybooks

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.support.v7.app.AppCompatDelegate
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.AppModule
import com.google.android.gms.ads.MobileAds
import org.jetbrains.anko.notificationManager
import toothpick.Toothpick


open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initAppScope()
        initNotificationChannel()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initAds()
    }

    private fun initAds() {
        MobileAds.initialize(this, "ca-app-pub-5580850164009775~6716592020")
    }

    private fun initAppScope() {
        val appScope = Toothpick.openScope(DI.APP_SCOPE)
        appScope.installModules(AppModule(this))
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setSound(null, null)
            }.let {
                notificationManager.createNotificationChannel(it)
            }
        }
    }

    companion object {
        const val BASE_NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "MyBooks notifications"
    }
}