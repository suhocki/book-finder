package app.suhocki.mybooks.ui.background

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.progress.ProgressStep
import app.suhocki.mybooks.ui.initial.InitialActivity
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class NotificationProvider @Inject constructor(
    private val context: Context
) {

    fun getProgressNotification(progressStep: ProgressStep): Notification = with(context) {
        val title = getString(R.string.step_info, progressStep.number, ProgressStep.values().size)
        val pendingIntent = createIntent(BackgroundCommand.CANCEL)
        val description = getString(progressStep.descriptionRes)
        getNotificationBuilder()
            .setContentTitle(title)
            .setContentText(description)
            .addAction(NotificationCompat.Action(0, getString(R.string.cancel), pendingIntent))
            .setSmallIcon(R.drawable.ic_download)
            .setShowWhen(false)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setProgress(BackgroundService.PROGRESS_MAX, progressStep.progress, false)
            .build()
            .apply { flags = Notification.FLAG_AUTO_CANCEL }
    }

    fun getCurrentStepNotification(step: ProgressStep): Notification = with(context) {
        val title = getString(R.string.step_info, step.number, ProgressStep.values().size)
        val pendingIntent = createIntent(BackgroundCommand.CANCEL)
        val description = getString(step.descriptionRes)
        getNotificationBuilder()
            .setContentTitle(title)
            .setShowWhen(false)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_download)
            .addAction(NotificationCompat.Action(0, getString(R.string.cancel), pendingIntent))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setProgress(0, 0, true)
            .build()
            .apply { flags = Notification.FLAG_AUTO_CANCEL }
    }

    fun getErrorNotification(@StringRes errorDescriptionRes: Int): Notification = with(context) {
        val title = getString(R.string.error)
        val description = getString(errorDescriptionRes)
        val pendingIntent = createIntent(BackgroundCommand.START)
        return getNotificationBuilder()
            .setSmallIcon(R.drawable.notification_error)
            .setContentTitle(title)
            .addAction(NotificationCompat.Action(0, getString(R.string.retry), pendingIntent))
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
            .apply { flags = Notification.FLAG_AUTO_CANCEL }
    }

    fun getSuccessNotification(statistics: Pair<Int, Int>): Notification = with(context) {
        val title = getString(R.string.success)
        val (categoriesCount, booksCount) = statistics
        val description = getString(R.string.downloading_statistics, booksCount, categoriesCount)
        val intentForContinue = createIntent(BackgroundCommand.CONTINUE)
        val intentForContent = intentFor<InitialActivity>()
            .apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
        val intentContent = PendingIntent
            .getActivity(this, 0, intentForContent, PendingIntent.FLAG_UPDATE_CURRENT)
        getNotificationBuilder()
            .setSmallIcon(R.drawable.ic_success)
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(intentContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(NotificationCompat.Action(0, getString(R.string._continue), intentForContinue))
            .setStyle(NotificationCompat.BigTextStyle())
            .setAutoCancel(true)
            .build()
            .apply { flags = Notification.FLAG_AUTO_CANCEL }
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder = with(context) {
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            intentFor<InitialActivity>(),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentIntent(contentIntent)
            .setSound(null)
            .setDefaults(0)
    }

    private fun createIntent(command: BackgroundCommand): PendingIntent = with(context) {
        val intent = intentFor<BackgroundService>(BackgroundService.COMMAND to command)
        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        private const val CHANNEL_ID = "MyBooks notifications"
    }
}