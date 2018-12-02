package app.suhocki.mybooks.data.service

import android.app.Activity
import android.app.ActivityManager
import android.os.Process
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.context.ContextManager
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.domain.model.admin.UploadControl
import app.suhocki.mybooks.ui.admin.background.UploadService
import app.suhocki.mybooks.ui.firestore.FirestoreService
import org.jetbrains.anko.startService
import javax.inject.Inject

class ServiceHandler @Inject constructor(
    private val contextManager: ContextManager,
    private val resourceManager: ResourceManager
) {

    fun startUploadService(file: File) {
        contextManager.currentContext
            .startService<UploadService>(UploadService.ARG_FILE to file)
    }

    fun killUploadServiceProcess() {
        val activityManager = contextManager.currentContext
            .getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        val processId = activityManager.runningAppProcesses.find {
            it.processName == contextManager.currentContext.packageName +
                    resourceManager.getString(R.string.upload_service_name)
        }?.pid
        processId?.let { Process.killProcess(it) }
    }

    fun startFirestoreService(
        @FirestoreService.UpdateCommand command: String,
        uploadControl: UploadControl? = null,
        categoryId: String? = null
    ) {
        contextManager.currentContext
            .startService<FirestoreService>(
                FirestoreService.ARG_COMMAND to command,
                FirestoreService.ARG_UPLOAD_CONTROL to uploadControl,
                FirestoreService.ARG_CATEGORY_ID to categoryId
            )
    }

    fun startFirestoreService(
        command: String,
        paginatedFrom: Int,
        paginatedTo: Int
    ) {

    }
}