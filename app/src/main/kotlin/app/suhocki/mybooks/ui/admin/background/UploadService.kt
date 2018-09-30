package app.suhocki.mybooks.ui.admin.background

import android.app.IntentService
import android.content.Intent
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.BackgroundInteractor
import app.suhocki.mybooks.domain.model.XlsDocument
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.domain.model.admin.UploadControl
import app.suhocki.mybooks.domain.repository.FileActionsRepository
import app.suhocki.mybooks.ui.admin.eventbus.UploadServiceEvent
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.AnkoLogger
import toothpick.Toothpick
import javax.inject.Inject

class UploadService : IntentService("UploadService"), AnkoLogger {

    @Inject
    lateinit var uploadControl: UploadControl

    @Inject
    lateinit var interactor: BackgroundInteractor

    @Inject
    lateinit var resourceManager: ResourceManager

    private lateinit var file: File
    private lateinit var strings: ArrayList<String>
    private lateinit var document: XlsDocument


    override fun onCreate() {
        super.onCreate()
        Toothpick.inject(this, Toothpick.openScopes(DI.ADMIN_SCOPE))
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
        EventBus.getDefault().postSticky(UploadServiceEvent(null))
    }

    private val tasks = mapOf(
        R.string.step_downloading to {
            EventBus.getDefault().postSticky(UploadServiceEvent(uploadControl))

            if (interactor.getDownloadedFile(file.id) == null) {
                val bytes = interactor.downloadFile(file.id)
                interactor.saveFile(file.id, file.name, bytes)
            }
        },

        R.string.step_unzipping to {
            EventBus.getDefault().postSticky(UploadServiceEvent(uploadControl))

            if (interactor.getUnzippedFile(file.id) == null) {
                val zipped = interactor.getDownloadedFile(file.id)!!
                val unzipped = interactor.unzip(zipped)
                val fileName = FileActionsRepository.Constants.UNZIPPED_FILE_PREFIX + unzipped.name
                interactor.saveFile(file.id, fileName, unzipped.readBytes())
            }
        },

        R.string.step_string_analysing to {
            EventBus.getDefault().postSticky(UploadServiceEvent(uploadControl))

            val unzipped = interactor.getUnzippedFile(file.id)!!
            strings = interactor.parseXlsStructure(unzipped)
        },

        R.string.step_book_construct to {
            EventBus.getDefault().postSticky(UploadServiceEvent(uploadControl))

            document = interactor.extractXlsDocument(strings)
        },

        R.string.step_saving_to_local to {
            EventBus.getDefault().postSticky(UploadServiceEvent(uploadControl))

            interactor.saveBooksData(document.booksData)
            interactor.saveStatisticsData(document.statisticsData)
            interactor.saveInfoData(document.infosData)
            interactor.saveBannersData(document.bannersData)
        },

        R.string.step_saving_to_remote to {
            EventBus.getDefault().postSticky(UploadServiceEvent(uploadControl))


        }
    )


    companion object {
        const val ARG_FILE = "ARG_FILE"
    }
}