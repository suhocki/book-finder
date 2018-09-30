package app.suhocki.mybooks.ui.admin

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.AdminInteractor
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.admin.File
import app.suhocki.mybooks.domain.model.admin.UploadControl
import app.suhocki.mybooks.ui.admin.entity.UploadControlEntity
import app.suhocki.mybooks.ui.admin.eventbus.ProgressEvent
import app.suhocki.mybooks.ui.licenses.entity.HeaderEntity
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class AdminPresenter @Inject constructor(
    private val interactor: AdminInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : MvpPresenter<AdminView>() {


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showProgress(true)

        doAsync(errorHandler.errorReceiver) {
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

    fun showProgress(data: List<Any>, progress: ProgressEvent) {
        val oldUploadControl = data.find { it is UploadControl } as UploadControl
        val file = data.find { it is File && it.name == oldUploadControl.fileName } as File
        progress.bytes?.let {
            progress.progress = (Math.abs(progress.bytes) / file.fileSize.toDouble() * 100).toInt()
        }
        val newUploadControl = UploadControlEntity(oldUploadControl)
            .apply { this.progress = progress.progress!! }

        if (newUploadControl.progress != oldUploadControl.progress) {
            doAsync {
                uiThread {
                    viewState.showData(
                        mutableListOf<Any>().apply {
                            addAll(data)
                            set(indexOf(oldUploadControl), newUploadControl)
                        }, false
                    )
                }
            }
        }
    }


    companion object {
        private const val HEADER_POSITION = 0
        private const val UPLOAD_CONTROL_POSITION = 1
    }
}