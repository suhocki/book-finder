package suhockii.dev.bookfinder.presentation.background

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import suhockii.dev.bookfinder.R
import suhockii.dev.bookfinder.data.error.ErrorHandler
import suhockii.dev.bookfinder.data.error.ErrorListener
import suhockii.dev.bookfinder.data.error.ErrorType
import suhockii.dev.bookfinder.data.notifier.ComponentCommandListener
import suhockii.dev.bookfinder.data.notifier.ComponentNotifier
import suhockii.dev.bookfinder.data.progress.ProgressHandler
import suhockii.dev.bookfinder.data.progress.ProgressListener
import suhockii.dev.bookfinder.data.progress.ProgressStep
import suhockii.dev.bookfinder.domain.BackgroundInteractor
import java.util.concurrent.Future
import javax.inject.Inject

@InjectViewState
class BackgroundPresenter @Inject constructor(
    private val interactor: BackgroundInteractor,
    private val progressHandler: ProgressHandler,
    private val errorHandler: ErrorHandler,
    private val componentNotifier: ComponentNotifier
) : MvpPresenter<BackgroundView>(), ErrorListener, ProgressListener, ComponentCommandListener {

    private lateinit var loadDatabaseTask: Future<Unit>

    init {
        errorHandler.addListener(this)
        progressHandler.addListener(this, UPDATE_INTERVAL)
    }

    fun loadDatabase() = doAsync(errorHandler.errorReceiver) {
        componentNotifier.addListener(this@BackgroundPresenter)
        componentNotifier.onLoadingStep(ProgressStep.DOWNLOADING)
        val bytes = interactor.downloadDatabaseFile()
        val zipFile = interactor.saveDatabaseFile(bytes)
        componentNotifier.onLoadingStep(ProgressStep.UNZIPPING)
        val unzippedFile = interactor.unzip(zipFile, zipFile.parentFile)
        componentNotifier.onLoadingStep(ProgressStep.ANALYZING)
        val unhandledStrings = interactor.parseXlsStructure(unzippedFile)
        componentNotifier.onLoadingStep(ProgressStep.PARSING)
        val xlsDocument = interactor.extractXlsDocument(unhandledStrings)
        componentNotifier.onLoadingStep(ProgressStep.SAVING)
        interactor.saveDocumentData(xlsDocument.data)
        val statistics = interactor.getBooksAndCategoriesCount()
        interactor.setDownloadStatistics(statistics)
        componentNotifier.onLoadingComplete(statistics)
        componentNotifier.removeListener(this@BackgroundPresenter)
    }.apply {
        loadDatabaseTask = this
    }

    fun stopDatabaseLoading() = doAsync(errorHandler.errorReceiver) {
        loadDatabaseTask.cancel(true)
        componentNotifier.onLoadingCancelled()
    }

    override fun onError(error: ErrorType) = when (error) {
        ErrorType.NETWORK -> R.string.error_network

        ErrorType.OUT_OF_MEMORY -> R.string.out_of_memory

        ErrorType.CORRUPTED_FILE -> R.string.corrupted_file

        ErrorType.UNKNOWN -> R.string.error_unknown
    }.let { viewState.showError(it) }

    override fun onProgress(progressStep: ProgressStep, done: Boolean) {
        viewState.showProgress(progressStep, done)
    }

    override fun onLoadingStep(step: ProgressStep) {
        viewState.showLoadingStep(step)
    }

    override fun onLoadingComplete(statistics: Pair<Int, Int>) {
        viewState.showSuccess(statistics)
    }

    override fun onLoadingCancelled() {
        viewState.cancelService()
    }

    override fun onDestroy() {
        super.onDestroy()
        errorHandler.removeListener(this)
        progressHandler.removeListener(this)
        componentNotifier.removeListener(this)
    }

    fun onTargetComponentForeground(currentStep: ProgressStep) = doAsync(errorHandler.errorReceiver) {
        if (currentStep.isAllCompleted) {
            val statistics = interactor.getBooksAndCategoriesCount()
            componentNotifier.onLoadingComplete(statistics)
        } else {
            componentNotifier.onLoadingStep(currentStep)
        }
    }

    companion object {
        private const val UPDATE_INTERVAL = 2000L
    }
}
