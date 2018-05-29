package app.suhocki.mybooks.ui.background

import android.app.Notification
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import app.suhocki.mybooks.App
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.BackgroundServiceModule
import app.suhocki.mybooks.isAppInBackground
import app.suhocki.mybooks.ui.base.MvpService
import app.suhocki.mybooks.ui.catalog.CatalogFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.notificationManager
import toothpick.Toothpick


class BackgroundService : MvpService(), BackgroundView {

    @InjectPresenter
    lateinit var presenter: BackgroundPresenter


    @ProvidePresenter
    fun providePresenter(): BackgroundPresenter =
        Toothpick.openScope(DI.APP_SCOPE)
            .apply {
                val downloadDirectory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).path
                installModules(BackgroundServiceModule(downloadDirectory))
            }
            .getInstance(BackgroundPresenter::class.java)

    override fun onCreate() {
        super.onCreate()
        Toothpick.openScopes(DI.APP_SCOPE, DI.BACKGROUND_SERVICE_SCOPE)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toothpick.closeScope(DI.BACKGROUND_SERVICE_SCOPE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return super.onStartCommand(intent, flags, startId)
        when (intent.getSerializableExtra(COMMAND) as BackgroundCommand) {
            BackgroundCommand.START -> presenter.loadDatabase()

            BackgroundCommand.CANCEL -> presenter.stopDatabaseLoading()

            BackgroundCommand.SYNC_STATE -> presenter.sendCurrentStateToComponents()

            BackgroundCommand.CONTINUE -> presenter.setAllLoaded()

            BackgroundCommand.STOP_FOREGROUND -> {
                stopForeground(true)
                notificationManager.cancelAll()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun showNotification(notification: Notification) {
        if (isAppInBackground()) {
            startForeground(App.BASE_NOTIFICATION_ID, notification)
        }
    }

    override fun stopForegroundMode(removeNotification: Boolean) {
        stopForeground(removeNotification)
    }

    override fun stopService() {
        notificationManager.cancelAll()
        stopForeground(true)
        stopSelf()
    }

    override fun showCatalogScreen() {
        intentFor<CatalogFragment>().let {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(it)
        }
    }

    companion object {
        const val COMMAND = "COMMAND"
        const val PROGRESS_MAX = 100
    }
}

