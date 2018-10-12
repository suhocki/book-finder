package app.suhocki.mybooks.ui.admin

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.data.service.ServiceHandler
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.domain.AdminInteractor
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.domain.model.admin.UploadControl
import app.suhocki.mybooks.ui.admin.entity.Progress
import app.suhocki.mybooks.ui.admin.entity.UploadControlEntity
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
    private val interactor: AdminInteractor,
    private val resourceManager: ResourceManager,
    private val serviceHandler: ServiceHandler
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
                addAll(interactor.getAvailableFiles())
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
        serviceHandler.startIntentService(file)
    }

    fun stopUpload(items: MutableList<Any>) {
        serviceHandler.killService()
        val newItems = mutableListOf<Any>().apply {
            add(HeaderEntity(resourceManager.getString(R.string.choose_file)))
            addAll(items)
            removeAll { it is UploadControl }
        }
        viewState.showData(newItems)
    }


    companion object {
        private const val UPLOADING_FILE_POSITION = 0
        private const val UPLOAD_CONTROL_POSITION = 1
        private const val PROGRESS_POSITION = 1
    }
}