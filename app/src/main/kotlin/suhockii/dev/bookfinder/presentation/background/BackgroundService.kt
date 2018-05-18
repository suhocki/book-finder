package suhockii.dev.bookfinder.presentation.background

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.notificationManager
import suhockii.dev.bookfinder.R
import suhockii.dev.bookfinder.data.progress.ProgressStep
import suhockii.dev.bookfinder.di.DI
import suhockii.dev.bookfinder.di.module.BackgroundServiceModule
import suhockii.dev.bookfinder.presentation.base.MvpService
import suhockii.dev.bookfinder.presentation.categories.CategoriesActivity
import suhockii.dev.bookfinder.presentation.initial.InitialActivity
import toothpick.Toothpick


class BackgroundService : MvpService(), BackgroundView {

    @InjectPresenter
    lateinit var presenter: BackgroundPresenter

    private var currentStep: ProgressStep? = null

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
        createNotificationChannel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toothpick.closeScope(DI.BACKGROUND_SERVICE_SCOPE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return super.onStartCommand(intent, flags, startId)
        when {
            intent.extras?.containsKey(ARG_ACTION_CANCEL)
                    ?: false -> presenter.stopDatabaseLoading()

            intent.extras?.containsKey(ARG_ACTION_RETRY) ?: false -> presenter.loadDatabase()

            else -> when (intent.getSerializableExtra(COMMAND) as Command) {
                Command.START -> presenter.loadDatabase()

                Command.CANCEL -> presenter.stopDatabaseLoading()

                Command.SYNC_STATE -> currentStep?.let { presenter.onTargetComponentForeground(it) }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun showLoadingStep(step: ProgressStep) {
        currentStep = step
        val title = getString(R.string.step_info, step.number, ProgressStep.values().size)
        val description = getString(currentStep!!.descriptionRes)
        val notification = getNotificationBuilder()
            .setContentTitle(title)
            .setShowWhen(false)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_download)
            .addAction(getCancelAction())
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setProgress(0, 0, true)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun showSuccess(statistics: Pair<Int, Int>) {
        currentStep!!.isAllCompleted = true
        val title = getString(R.string.success)
        val (categoriesCount, booksCount) = statistics
        val description = getString(R.string.downloading_statistics, booksCount, categoriesCount)
        val intentForContinue = intentFor<CategoriesActivity>(CategoriesActivity.ARG_FROM_NOTIFICATION to null)
        val intentContinue = PendingIntent.getActivity(this, 0, intentForContinue, 0)
        val intentForContent = intentFor<InitialActivity>()
            .apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
        val intentContent = PendingIntent
            .getActivity(this, 0, intentForContent, PendingIntent.FLAG_UPDATE_CURRENT)
        val style = NotificationCompat.BigTextStyle()
        val notification = getNotificationBuilder()
            .setSmallIcon(R.drawable.ic_success)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(intentContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(NotificationCompat.Action(0, getString(R.string._continue), intentContinue))
            .setStyle(style)
            .setAutoCancel(true)
            .build()
            .apply { flags = Notification.FLAG_AUTO_CANCEL }

        stopForeground(true)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun showError(errorDescriptionRes: Int) {
        currentStep = null
        val title = getString(R.string.error)
        val description = getString(errorDescriptionRes)
        val notification = getNotificationBuilder()
            .setSmallIcon(R.drawable.notification_error)
            .setContentTitle(title)
            .addAction(getRetryAction())
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun getCancelAction(): NotificationCompat.Action {
        val intent = intentFor<BackgroundService>(ARG_ACTION_CANCEL to null)
        val pendingIntent =
            PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Action(0, getString(R.string.cancel), pendingIntent)
    }

    private fun getRetryAction(): NotificationCompat.Action {
        val intent = intentFor<BackgroundService>(ARG_ACTION_RETRY to null)
        val pendingIntent =
            PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return NotificationCompat.Action(0, getString(R.string.retry), pendingIntent)
    }

    override fun cancelService() {
        notificationManager.cancelAll()
        stopForeground(true)
        stopSelf()
    }

    override fun showProgress(progressStep: ProgressStep, done: Boolean) {
        currentStep = progressStep
        val title = getString(R.string.step_info, currentStep!!.number, ProgressStep.values().size)
        val description = getString(currentStep!!.descriptionRes)
        val notification = getNotificationBuilder()
            .setContentTitle(title)
            .setContentText(description)
            .addAction(getCancelAction())
            .setSmallIcon(R.drawable.ic_download)
            .setShowWhen(false)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setProgress(PROGRESS_MAX, progressStep.progress, false)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                .apply { setSound(null, null) }
                .let { notificationManager.createNotificationChannel(it) }
        }
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intentFor<InitialActivity>(),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSound(null)
            .setDefaults(0)
    }

    companion object {
        const val COMMAND = "COMMAND"
        const val NOTIFICATION_ID = 1
        const val PROGRESS_MAX = 100
        private const val CHANNEL_ID = "Book-Finder notifications"
        private const val CHANNEL_NAME = "Book-Finder channel"
        private const val ARG_ACTION_CANCEL = "ARG_ACTION_CANCEL"
        private const val ARG_ACTION_RETRY = "ARG_ACTION_RETRY"
    }
}

