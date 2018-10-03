package app.suhocki.mybooks.ui.admin

import android.content.Context
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.dialog.DialogManager
import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.domain.AdminInteractor
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.ResponseKind
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.domain.model.admin.UploadControl
import app.suhocki.mybooks.ui.admin.entity.UploadControlEntity
import app.suhocki.mybooks.ui.admin.eventbus.ProgressEvent
import app.suhocki.mybooks.ui.base.eventbus.ErrorEvent
import app.suhocki.mybooks.ui.licenses.entity.HeaderEntity
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.run
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class AdminPresenter @Inject constructor(
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    private val interactor: AdminInteractor,
    private val resourceManager: ResourceManager,
    private val mapper: Mapper,
    private val dialogManager: DialogManager
) : MvpPresenter<AdminView>() {


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadFiles()
    }

    fun loadFiles() {
        viewState.showData(listOf())
        viewState.showError(isVisible = false)
        viewState.showJsonProgress(true)
        doAsync({ throwable ->
            doAsync { uiThread { viewState.showJsonProgress(false) } }
            errorReceiver(throwable)
        }) {
            val data = mutableListOf<Any>().apply {
                add(HeaderEntity(resourceManager.getString(R.string.choose_file)))
                addAll(interactor.getAvailableFiles())
            }
            uiThread {
                viewState.showJsonProgress(false)
                viewState.showData(data)
            }
        }
    }

    fun insertUploadControl(
        data: MutableList<Any>,
        uploadControl: UploadControl
    ) = mutableListOf<Any>().apply {
        addAll(data)
        removeAll { it is UploadControl || it is Header }
        add(HEADER_POSITION, HeaderEntity(uploadControl.fileName))
        add(UPLOAD_CONTROL_POSITION, UploadControlEntity(uploadControl))
        viewState.showData(this)
    }

    fun removeUploadControl(data: MutableList<Any>) = mutableListOf<Any>().apply {
        addAll(data)
        set(HEADER_POSITION, HeaderEntity(resourceManager.getString(R.string.choose_file)))
        removeAt(UPLOAD_CONTROL_POSITION)
        viewState.showData(this)
    }

    fun onDownloadProgress(data: List<Any>?, event: ProgressEvent) {
        if (event.downloadUrl != null) {
            when (mapper.map(event.downloadUrl!!, ResponseKind::class.java)) {
                ResponseKind.JSON -> onJsonDownloadProgress(event)

                ResponseKind.FILE -> onFileDownloadProgress(data!!, event)
            }
        } else {
            onFileDownloadProgress(data!!, event)
        }
    }

    private fun onJsonDownloadProgress(event: ProgressEvent) =
        doAsync { uiThread { viewState.showJsonProgress(true, event.progress) } }

    private fun onFileDownloadProgress(
        data: List<Any>,
        event: ProgressEvent
    ) {
        val oldUploadControl = data.find { it is UploadControl } as UploadControl
        val file = data.find { it is File && it.name == oldUploadControl.fileName } as File
        event.bytes?.let {
            event.progress = (Math.abs(event.bytes) / file.fileSize.toDouble() * 100).toInt()
        }

        val newUploadControl = UploadControlEntity(oldUploadControl)
            .apply { this.progress = event.progress }

        val changedPosition = data.indexOf(oldUploadControl)

        val newData = mutableListOf<Any>().apply {
            addAll(data)
            set(changedPosition, newUploadControl)
        }

        if (newUploadControl.progress != oldUploadControl.progress) {
            doAsync {
                uiThread {
                    viewState.showData(newData, changedPosition, newUploadControl.progress)
                }
            }
        }
    }

    fun onError(context: Context, errorEvent: ErrorEvent) {
        viewState.showError(errorEvent.messageRes)
        dialogManager.showErrorDialog(context, errorEvent.messageRes) { loadFiles() }
    }


    companion object {
        private const val HEADER_POSITION = 0
        private const val UPLOAD_CONTROL_POSITION = 1
    }
}