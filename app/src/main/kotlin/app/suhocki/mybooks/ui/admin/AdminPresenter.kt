package app.suhocki.mybooks.ui.admin

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.googledrive.GoogleDriveRepository
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.data.service.ServiceHandler
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.domain.model.admin.UploadControl
import app.suhocki.mybooks.model.system.flow.FlowRouter
import app.suhocki.mybooks.ui.admin.entity.UploadControlEntity
import app.suhocki.mybooks.ui.base.entity.Progress
import app.suhocki.mybooks.ui.base.eventbus.ErrorEvent
import app.suhocki.mybooks.ui.licenses.entity.HeaderEntity
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class AdminPresenter @Inject constructor(
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    private val resourceManager: ResourceManager,
    private val googleDriveRepository: GoogleDriveRepository,
    private val serviceHandler: ServiceHandler,
    private val router: FlowRouter
) : MvpPresenter<AdminView>() {


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadFiles()
    }

    fun loadFiles() {
        viewState.showError(isVisible = false)
        viewState.showProgress(true)
        doAsync({ throwable ->
            doAsync { uiThread { viewState.showProgress(false) } }
            errorReceiver(throwable)
        }) {
            val data = mutableListOf<Any>().apply {
                add(HeaderEntity(resourceManager.getString(R.string.choose_file)))

                val folderId = resourceManager.getString(R.string.google_drive_folder_id)
                addAll(googleDriveRepository.getFiles(folderId))
            }
            uiThread {
                viewState.showProgress(false)
                viewState.showData(data)
            }
        }
    }

    fun insertUploadControl(
        data: MutableList<Any>,
        uploadControl: UploadControl
    ) = mutableListOf<Any>().apply {
        addAll(data)
        removeAll { it is UploadControl || it is Progress || it is Header }
        add(UPLOAD_CONTROL_POSITION, UploadControlEntity(uploadControl))
        viewState.showData(this)
    }

    fun onError(errorEvent: ErrorEvent) {
        viewState.showData(listOf())
        viewState.showError(errorEvent.messageRes)
        viewState.showErrorDialog(errorEvent.messageRes)
    }

    fun upload(file: File, items: MutableList<Any>) = mutableListOf<Any>().apply {
        addAll(items)
        removeAll { it is Header || it is Progress || (it is File && it.id == file.id) }
        add(UPLOADING_FILE_POSITION, file)
        add(PROGRESS_POSITION, Progress())

        viewState.showData(this)
        serviceHandler.startUploadService(file)
    }

    fun stopUpload(
        items: MutableList<Any>,
        shouldKillService: Boolean
    ) {
        if (shouldKillService) serviceHandler.killUploadServiceProcess()
        val newItems = mutableListOf<Any>().apply {
            addAll(items)
            removeAll { it is UploadControl || it is Header }
            add(0, HeaderEntity(resourceManager.getString(R.string.choose_file)))
        }
        viewState.showData(newItems)
    }

    fun onBackPressed() = router.exit()


    companion object {
        private const val UPLOADING_FILE_POSITION = 0
        private const val UPLOAD_CONTROL_POSITION = 1
        private const val PROGRESS_POSITION = 1
    }
}