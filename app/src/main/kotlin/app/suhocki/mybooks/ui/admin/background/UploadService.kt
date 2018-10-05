package app.suhocki.mybooks.ui.admin.background

import android.app.IntentService
import android.content.Intent
import android.os.Environment
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.localstorage.LocalFilesRepository
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.UploadServiceModule
import app.suhocki.mybooks.domain.BackgroundInteractor
import app.suhocki.mybooks.domain.model.XlsDocument
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.ui.admin.eventbus.DatabaseUpdatedEvent
import app.suhocki.mybooks.ui.admin.eventbus.UploadServiceEvent
import app.suhocki.mybooks.ui.base.mpeventbus.MPEventBus
import org.jetbrains.anko.AnkoLogger
import toothpick.Toothpick
import javax.inject.Inject

class UploadService : IntentService("UploadService"), AnkoLogger {

    @Inject
    lateinit var interactor: BackgroundInteractor

    @Inject
    lateinit var resourceManager: ResourceManager

    private val uploadControl by lazy {
        val firstStepId = resourceManager
            .getStringArrayIdentifiers(R.array.database_upload_steps).first()
        UploadControlEntity(stepRes = firstStepId)
    }

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
            tasks[it]!!.invoke()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MPEventBus.getDefault().postToAll(UploadServiceEvent(null))
    }

    private val tasks = mapOf(
        R.string.step_downloading to {
            MPEventBus.getDefault().postToAll(UploadServiceEvent(uploadControl))

            if (interactor.getDownloadedFile(file.id) == null) {
                val bytes = interactor.downloadFile(file.id)
                interactor.saveFile(file.id, file.name, bytes)
            }
        },

        R.string.step_unzipping to {
            MPEventBus.getDefault().postToAll(UploadServiceEvent(uploadControl))

            if (interactor.getUnzippedFile(file.id) == null) {
                val zipped = interactor.getDownloadedFile(file.id)!!
                val unzipped = interactor.unzip(zipped)
                val fileName = LocalFilesRepository.UNZIPPED_FILE_PREFIX + unzipped.name
                interactor.saveFile(file.id, fileName, unzipped.readBytes())
            }
        },

        R.string.step_string_analysing to {
            MPEventBus.getDefault().postToAll(UploadServiceEvent(uploadControl))

            val unzipped = interactor.getUnzippedFile(file.id)!!
            strings = interactor.parseXlsStructure(unzipped)
        },

        R.string.step_book_construct to {
            MPEventBus.getDefault().postToAll(UploadServiceEvent(uploadControl))

            document = interactor.extractXlsDocument(strings)
        },

        R.string.step_saving_to_local to {
            MPEventBus.getDefault().postToAll(UploadServiceEvent(uploadControl))

            interactor.saveBooksData(document.booksData)
            interactor.saveStatisticsData(document.statisticsData)
            interactor.saveInfoData(document.infosData)
            interactor.saveBannersData(document.bannersData)
        },

        R.string.step_saving_to_remote to {
            MPEventBus.getDefault().postToAll(UploadServiceEvent(uploadControl))
            MPEventBus.getDefault().postToAll(DatabaseUpdatedEvent())
        }
    )


    companion object {
        const val ARG_FILE = "ARG_FILE"
    }
}