package app.suhocki.mybooks

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.support.v7.app.AppCompatDelegate
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.AppModule
import app.suhocki.mybooks.di.module.RoomModule
import app.suhocki.mybooks.ui.base.mpeventbus.MPEventBus
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import org.jetbrains.anko.notificationManager
import toothpick.Toothpick


@Suppress("unused")
@SuppressLint("Registered")
open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initAppScope()
        initNotificationChannel()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initAds()
        initFresco()
        MPEventBus.init(applicationContext)
    }

    private fun initAppScope() {
        val appScope = Toothpick.openScope(DI.APP_SCOPE)
        appScope.installModules(
            AppModule(this),
            RoomModule()
        )
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

    private fun initAds() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))
    }

    private fun initFresco() {
        val config = ImagePipelineConfig.newBuilder(this)
            .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
            .setResizeAndRotateEnabledForNetwork(true)
            .setDownsampleEnabled(true)
            .build()
        Fresco.initialize(this, config)
    }

    companion object {
        const val BASE_NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "MyBooks notifications"
    }
}