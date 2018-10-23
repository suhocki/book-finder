package app.suhocki.mybooks.data.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.context.ContextManager
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.ui.admin.background.UploadService
import app.suhocki.mybooks.ui.base.TabPosition
import app.suhocki.mybooks.ui.main.MainActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.notificationManager
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    private val contextManager: ContextManager,
    private val resourceManager: ResourceManager
) {
    private val context: Context
        get() = contextManager.applicationContext
    private val notificationManager: NotificationManager
        get() = context.notificationManager

    init {
        createNotificationChannel()
    }

    fun showProgressNotification(@StringRes stepId: Int, progress: Int) {
        val stepIds = resourceManager.getStringArrayIdentifiers(R.array.database_upload_steps)
        val currentStepIndex = stepIds.indexOf(stepId)
        val title =
            context.resources.getString(R.string.step_info, currentStepIndex.inc(), stepIds.size)

        val cancelIntent = PendingIntent.getService(
            context, 0,
            context.intentFor<UploadService>(
                UploadService.ARG_COMMAND to UploadService.Command.CANCEL
            ),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val description = context.resources.getString(stepId)
        val cancelText = context.resources.getString(R.string.cancel)

        val notification = getNotificationBuilder(MainActivity.TabPositions.TAB_POSITION_ADMIN)
            .setContentTitle(title)
            .setShowWhen(false)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_download_file)
            .setProgress(UploadService.PROGRESS_MAX, progress, progress == 0)
            .addAction(NotificationCompat.Action(0, cancelText, cancelIntent))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
            .apply { flags = Notification.FLAG_AUTO_CANCEL }
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun showErrorNotification(@StringRes errorDescriptionRes: Int): Notification = with(context) {
        val title = getString(R.string.error)
        val description = getString(errorDescriptionRes)
        val pendingIntent = createPendingIntent()
        return getNotificationBuilder(MainActivity.TabPositions.TAB_POSITION_ADMIN)
            .setSmallIcon(R.drawable.notification_error)
            .setContentTitle(title)
            .addAction(NotificationCompat.Action(0, getString(R.string.retry), pendingIntent))
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
            .apply { flags = Notification.FLAG_AUTO_CANCEL }
    }

    fun showSuccessNotification(fileName: String) {
        val title = context.getString(R.string.success)
        val intentForContent = context.intentFor<MainActivity>()
            .apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
        val intentContent = PendingIntent
            .getActivity(context, 0, intentForContent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = getNotificationBuilder(MainActivity.TabPositions.TAB_POSITION_ADMIN)
            .setSmallIcon(R.drawable.ic_success)
            .setContentTitle(title)
            .setContentText(context.getString(R.string.file_uploaded, fileName))
            .setSubText(context.getString(R.string.file_uploaded, fileName))
            .setContentIntent(intentContent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setStyle(NotificationCompat.BigTextStyle())
            .setAutoCancel(true)
            .build()
            .apply { flags = Notification.FLAG_AUTO_CANCEL }
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun getNotificationBuilder(@TabPosition tabPosition: Int): NotificationCompat.Builder =
        with(context) {
            val contentIntent = PendingIntent.getActivity(
                this,
                0,
                intentFor<MainActivity>(MainActivity.SCREEN_TAG to tabPosition),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            return NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setSound(null)
                .setDefaults(0)
        }

    private fun createPendingIntent(): PendingIntent = PendingIntent.getService(
        context,
        0,
        context.intentFor<UploadService>(),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                .apply { setSound(null, null) }
                .let { notificationManager.createNotificationChannel(it) }
        }
    }

    companion object {
        private const val CHANNEL_ID = "MyBooks notifications"
        private const val CHANNEL_NAME = "MyBooks channel"
        const val NOTIFICATION_ID = 1
    }
}