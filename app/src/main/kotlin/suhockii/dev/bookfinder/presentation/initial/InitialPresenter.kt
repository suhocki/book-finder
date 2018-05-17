package suhockii.dev.bookfinder.presentation.initial

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import suhockii.dev.bookfinder.R
import suhockii.dev.bookfinder.data.error.ErrorHandler
import suhockii.dev.bookfinder.data.error.ErrorListener
import suhockii.dev.bookfinder.data.error.ErrorType
import suhockii.dev.bookfinder.data.notifier.ComponentCommandListener
import suhockii.dev.bookfinder.data.notifier.ComponentNotifier
import suhockii.dev.bookfinder.data.progress.ProgressHandler
import suhockii.dev.bookfinder.data.progress.ProgressListener
import suhockii.dev.bookfinder.data.progress.ProgressStep
import suhockii.dev.bookfinder.domain.InitialInteractor
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

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initBackgroundService()
    }

    override fun attachView(view: InitialView) {
        super.attachView(view)
        errorHandler.invokeLastError()
        viewState.synchronizeWithBackground()
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
