package app.suhocki.mybooks.presentation.initial

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.error.ErrorListener
import app.suhocki.mybooks.data.error.ErrorType
import app.suhocki.mybooks.data.notifier.ComponentCommandListener
import app.suhocki.mybooks.data.notifier.ComponentNotifier
import app.suhocki.mybooks.data.progress.ProgressHandler
import app.suhocki.mybooks.data.progress.ProgressListener
import app.suhocki.mybooks.data.progress.ProgressStep
import app.suhocki.mybooks.domain.InitialInteractor
import javax.inject.Inject

@InjectViewState
class InitialPresenter @Inject constructor(
    private val interactor: InitialInteractor,
    private val errorHandler: ErrorHandler,
    private val progressHandler: ProgressHandler,
    private val componentNotifier: ComponentNotifier
) : MvpPresenter<InitialView>(), ComponentCommandListener, ErrorListener, ProgressListener {

    init {
        componentNotifier.addListener(this)
        errorHandler.addListener(this)
        progressHandler.addListener(this, UPDATE_INTERVAL)
    }

    override fun attachView(view: InitialView) {
        super.attachView(view)
        errorHandler.invokeLastError()
        viewState.synchronizeWithBackground()
        interactor.getDownloadStatistics()?.let { viewState.showSuccess(it) }
    }

    fun flowFinished() =
        doAsync(errorHandler.errorReceiver) {
            interactor.setDatabaseLoaded()
            uiThread { viewState.showMainScreen() }
        }

    override fun onLoadingStep(step: ProgressStep) {
        doAsync(errorHandler.errorReceiver) {
            uiThread { viewState.showLoadingStep(step) }
        }
    }

    override fun onLoadingComplete(statistics: Pair<Int, Int>) {
        doAsync(errorHandler.errorReceiver) {
            uiThread { viewState.showSuccess(statistics) }
        }
    }

    override fun onLoadingCancelled() {
        doAsync {
            uiThread { viewState.showInitialState() }
        }
    }

    override fun onError(error: ErrorType) = when (error) {
        ErrorType.NETWORK -> R.string.error_network

        ErrorType.OUT_OF_MEMORY -> R.string.out_of_memory

        ErrorType.CORRUPTED_FILE -> R.string.corrupted_file

        ErrorType.UNKNOWN -> R.string.error_unknown
    }.let { errorRes ->
        doAsync { uiThread { viewState.showError(errorRes) } }
    }

    override fun onProgress(progressStep: ProgressStep, done: Boolean) {
        doAsync { uiThread { viewState.showProgress(progressStep, done) } }
    }

    override fun onDestroy() {
        super.onDestroy()
        componentNotifier.removeListener(this)
        errorHandler.removeListener(this)
        progressHandler.removeListener(this)
    }

    companion object {
        private const val UPDATE_INTERVAL = 100L
    }
}
