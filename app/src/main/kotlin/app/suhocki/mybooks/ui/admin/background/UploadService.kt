package app.suhocki.mybooks.ui.admin.background

import android.app.IntentService
import android.content.Intent
import android.os.Environment
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.localstorage.LocalFilesRepository
import app.suhocki.mybooks.data.notification.NotificationHelper
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.UploadServiceModule
import app.suhocki.mybooks.domain.BackgroundInteractor
import app.suhocki.mybooks.domain.model.XlsDocument
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.ui.admin.eventbus.DatabaseUpdatedEvent
import app.suhocki.mybooks.ui.admin.eventbus.ServiceKilledEvent
import app.suhocki.mybooks.ui.base.mpeventbus.MPEventBus
import org.jetbrains.anko.AnkoLogger
import toothpick.Toothpick
import javax.inject.Inject

class UploadService : IntentService("UploadService"), AnkoLogger {

    @Inject
    lateinit var interactor: BackgroundInteractor

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var resourceManager: ResourceManager

    @Inject
    lateinit var uploadControl: UploadServiceModule.UploadControlEntity

    private lateinit var file: File
    private lateinit var strings: ArrayList<String>
    private lateinit var document: XlsDocument


    override fun onCreate() {
        super.onCreate()

        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.UPLOAD_SERVICE)
        val uploadServiceModule = UploadServiceModule(
            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).path
        )

        scope.installModules(uploadServiceModule)

        Toothpick.inject(this, scope)
    }

    override fun onHandleIntent(intent: Intent) {
        file = intent.getSerializableExtra(ARG_FILE) as File
        uploadControl.fileName = file.name

        val taskIds = resourceManager
            .getStringArrayIdentifiers(R.array.database_upload_steps)

        taskIds.forEach {
            uploadControl.stepRes = it
            uploadControl.sendProgress(0)
            notificationHelper.showProgressNotification(it, 0)
            tasks[it]!!.invoke()
        }
    }

    private val tasks = mapOf(
        R.string.step_downloading to {
            if (interactor.getDownloadedFile(file.id) == null) {
                val bytes = interactor.downloadFile(file.id)
                interactor.saveFile(file.id, file.name, bytes)
            }
        },

        R.string.step_unzipping to {
            uploadControl.sendProgress(0)

            if (interactor.getUnzippedFile(file.id) == null) {
                val zipped = interactor.getDownloadedFile(file.id)!!
                val unzipped = interactor.unzip(zipped)
                val fileName = LocalFilesRepository.UNZIPPED_FILE_PREFIX + unzipped.name
                interactor.saveFile(file.id, fileName, unzipped.readBytes())
            }
        },

        R.string.step_string_analysing to {
            val unzipped = interactor.getUnzippedFile(file.id)!!
            strings = interactor.parseXlsStructure(unzipped)
        },

        R.string.step_book_construct to {
            document = interactor.extractXlsDocument(strings)
        },

        R.string.step_saving_to_local to {
            interactor.saveBooksData(document.booksData)
            interactor.saveStatisticsData(document.statisticsData)
            interactor.saveInfoData(document.infosData)
            interactor.saveBannersData(document.bannersData)

            MPEventBus.getDefault().postToAll(DatabaseUpdatedEvent())
        },

        R.string.step_saving_to_remote to {
        }
    )

    override fun onDestroy() {
        super.onDestroy()
        MPEventBus.getDefault().postToAll(ServiceKilledEvent())
    }

    companion object {
        const val ARG_FILE = "ARG_FILE"
        const val COMMAND = "COMMAND"
        const val PROGRESS_MAX = 100
    }
}