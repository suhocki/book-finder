package app.suhocki.mybooks.ui.background

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.error.ErrorListener
import app.suhocki.mybooks.data.error.ErrorType
import app.suhocki.mybooks.data.notifier.ComponentCommandListener
import app.suhocki.mybooks.data.notifier.ComponentNotifier
import app.suhocki.mybooks.data.progress.ProgressHandler
import app.suhocki.mybooks.data.progress.ProgressListener
import app.suhocki.mybooks.data.progress.ProgressStep
import app.suhocki.mybooks.domain.BackgroundInteractor
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import java.util.concurrent.Future
import javax.inject.Inject

@InjectViewState
class BackgroundPresenter @Inject constructor(
    private val interactor: BackgroundInteractor,
    private val progressHandler: ProgressHandler,
    private val errorHandler: ErrorHandler,
    private val componentNotifier: ComponentNotifier,
    private val notificationProvider: NotificationProvider
) : MvpPresenter<BackgroundView>(), ErrorListener, ProgressListener, ComponentCommandListener {

    private lateinit var loadDatabaseTask: Future<Unit>
    private var currentStep: ProgressStep? = null

    init {
        errorHandler.addListener(this)
        progressHandler.addListener(this, UPDATE_INTERVAL)
    }

    fun loadDatabase() = doAsync(errorHandler.errorReceiver) {
        errorHandler.clearLastError()
        componentNotifier.addListener(this@BackgroundPresenter)
        componentNotifier.onProgressStep(ProgressStep.DOWNLOADING)
        val bytes = interactor.downloadDatabaseFile()
        val zipFile = interactor.saveDatabaseFile(bytes)
        componentNotifier.onProgressStep(ProgressStep.UNZIPPING)
        val unzippedFile = interactor.unzip(zipFile, zipFile.parentFile)
        componentNotifier.onProgressStep(ProgressStep.ANALYZING)
        val unhandledStrings = interactor.parseXlsStructure(unzippedFile)
        componentNotifier.onProgressStep(ProgressStep.PARSING)
        val xlsDocument = interactor.extractXlsDocument(unhandledStrings)
        componentNotifier.onProgressStep(ProgressStep.SAVING)
        interactor.saveBooksData(xlsDocument.booksData)
        interactor.saveStatisticsData(xlsDocument.statisticsData)
        val statistics = interactor.getBooksAndCategoriesCount()
        interactor.setDownloadStatistics(statistics)
        componentNotifier.onLoadingComplete(statistics)
        componentNotifier.removeListener(this@BackgroundPresenter)
    }.apply {
        loadDatabaseTask = this
    }

    fun stopDatabaseLoading() = doAsync(errorHandler.errorReceiver) {
        currentStep = null
        loadDatabaseTask.cancel(true)
        componentNotifier.onLoadingCancelled()
    }

    override fun onError(error: ErrorType) = when (error) {
        ErrorType.NETWORK -> R.string.error_network

        ErrorType.OUT_OF_MEMORY -> R.string.out_of_memory

        ErrorType.CORRUPTED_FILE -> R.string.corrupted_file

        ErrorType.UNKNOWN -> R.string.error_unknown
    }.let {
        currentStep = null
        val notification = notificationProvider.getErrorNotification(it)
        viewState.stopForegroundMode()
        viewState.showNotification(notification, false)
    }

    override fun onProgress(progressStep: ProgressStep, done: Boolean) {
        val notification = notificationProvider.getProgressNotification(progressStep)
        viewState.showNotification(notification, true)
    }

    override fun onLoadingStep(step: ProgressStep) {
        currentStep = step
        val notification = notificationProvider.getCurrentStepNotification(step)
        viewState.showNotification(notification, true)
    }

    override fun onLoadingComplete(statistics: Pair<Int, Int>) {
        currentStep!!.isAllCompleted = true
        val notification = notificationProvider.getSuccessNotification(statistics)
        viewState.stopForegroundMode()
        viewState.showNotification(notification, false)
    }

    override fun onLoadingCancelled() {
        viewState.stopService()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (currentStep != null) stopDatabaseLoading()
        errorHandler.removeListener(this)
        progressHandler.removeListener(this)
        componentNotifier.removeListener(this)
    }

    fun sendCurrentStateToComponents() = currentStep?.let {
        doAsync(errorHandler.errorReceiver) {
            if (currentStep!!.isAllCompleted) {
                val statistics = interactor.getBooksAndCategoriesCount()
                componentNotifier.onLoadingComplete(statistics)
            } else {
                componentNotifier.onProgressStep(currentStep!!)
            }
        }
    }

    fun setAllLoaded() {
        interactor.setDatabaseLoaded()
        viewState.showCatalogScreen()
        viewState.stopService()
    }

    companion object {
        private const val UPDATE_INTERVAL = 2000L
    }
}
