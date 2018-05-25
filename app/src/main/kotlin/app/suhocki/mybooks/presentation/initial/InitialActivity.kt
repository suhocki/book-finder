package app.suhocki.mybooks.presentation.initial

import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.progress.ProgressStep
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.InitialActivityModule
import app.suhocki.mybooks.presentation.background.BackgroundCommand
import app.suhocki.mybooks.presentation.background.BackgroundService
import app.suhocki.mybooks.presentation.catalog.CatalogActivity
import app.suhocki.mybooks.setGone
import app.suhocki.mybooks.setVisible
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textResource
import toothpick.Toothpick
import javax.inject.Inject


class InitialActivity : MvpAppCompatActivity(), InitialView {

    @InjectPresenter
    lateinit var presenter: InitialPresenter

    @Inject
    lateinit var layout: InitialUI

    @Inject
    lateinit var serviceConnection: ServiceConnection

    @ProvidePresenter
    fun providePresenter(): InitialPresenter =
        Toothpick.openScopes(DI.APP_SCOPE)
            .apply { installModules(InitialActivityModule()) }
            .getInstance(InitialPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.openScopes(DI.APP_SCOPE, DI.INITIAL_ACTIVITY_SCOPE)
            .apply { Toothpick.inject(this@InitialActivity, this) }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        layout.setContentView(this)
    }

    override fun onStart() {
        super.onStart()
        intentFor<BackgroundService>().let {
            bindService(it, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Toothpick.closeScope(DI.INITIAL_ACTIVITY_SCOPE)
    }

    override fun showLoadingStep(step: ProgressStep) {
        layout.textTitle.text =
                getString(R.string.step_info, step.number, ProgressStep.values().size)
        when (step) {
            ProgressStep.DOWNLOADING -> showLoading()

            ProgressStep.UNZIPPING -> showUnzipping()

            ProgressStep.ANALYZING -> showAnalyzing()

            ProgressStep.PARSING -> showParsing()

            ProgressStep.SAVING -> showSaving()
        }
    }

    override fun showProgress(progressStep: ProgressStep, done: Boolean) = with(layout) {
        setVisible(btnInBackground, textProgress, progressBar, btnCancel)
        setGone(btnExit, btnRetry, btnDownload, btnContinue)
        if (done) {
            textProgress.text = ""
            setGone(textProgress)
        } else {
            textProgress.text = getString(R.string.percent, progressStep.progress)
            textProgress.visibility = View.VISIBLE
        }
    }

    private fun showLoading() = with(layout) {
        textProgress.text = getString(R.string.percent, 0)
        textDescription.textResource = R.string.downloading
        setVisible(btnInBackground, textProgress, progressBar, btnCancel)
        setGone(btnExit, btnRetry, btnDownload, btnContinue)
    }

    private fun showUnzipping() = with(layout) {
        textDescription.textResource = R.string.unzipping
        setVisible(btnInBackground, progressBar, btnCancel)
        setGone(textProgress, btnExit, btnRetry, btnDownload, btnContinue)
    }

    private fun showAnalyzing() = with(layout) {
        layout.textDescription.textResource = R.string.analyzing
        setVisible(btnInBackground, progressBar, btnCancel)
        setGone(textProgress, btnExit, btnRetry, btnDownload, btnContinue)
    }

    private fun showParsing() = with(layout) {
        layout.textDescription.textResource = R.string.parsing
        setVisible(btnInBackground, textProgress, progressBar, btnCancel)
        setGone(btnExit, btnRetry, btnDownload, btnContinue)
    }

    private fun showSaving() = with(layout) {
        layout.textDescription.textResource = R.string.saving
        setVisible(btnInBackground, progressBar, btnCancel)
        setGone(textProgress, btnExit, btnRetry, btnDownload, btnContinue)
    }

    override fun showSuccess(statistics: Pair<Int, Int>) = with(layout) {
        val (categoriesCount, booksCount) = statistics
        textTitle.textResource = R.string.success
        val statisticsRes = R.string.downloading_statistics
        textDescription.text = getString(statisticsRes, booksCount, categoriesCount)
        val drawableSuccess = ContextCompat.getDrawable(ivTop.context, R.drawable.ic_success)
        ivTop.setImageDrawable(drawableSuccess)

        setVisible(btnContinue)
        setGone(
            btnInBackground, btnCancel, progressBar, textProgress, btnExit, btnRetry,
            btnDownload
        )
    }

    override fun showMainScreen() {
        startActivity<CatalogActivity>()
        finish()
    }

    override fun showInitialState() = with(layout) {
        textTitle.text = getString(R.string.info)
        textDescription.textResource = R.string.database_load_need
        setVisible(btnDownload, btnExit)
        setGone(btnInBackground, btnCancel, progressBar, textProgress, btnRetry)
    }

    override fun showError(errorDescriptionRes: Int) = with(layout) {
        textTitle.textResource = R.string.error
        textDescription.textResource = errorDescriptionRes
        setVisible(btnRetry, btnExit)
        setGone(btnDownload, btnInBackground, btnCancel, progressBar, textProgress)
    }

    override fun synchronizeWithBackground() {
        intentFor<BackgroundService>(BackgroundService.COMMAND to BackgroundCommand.SYNC_STATE)
            .let { startService(it) }
    }

    fun startDownloading() {
        intentFor<BackgroundService>(BackgroundService.COMMAND to BackgroundCommand.START)
            .let { startService(it) }
    }

    fun cancelDownloading() {
        intentFor<BackgroundService>(BackgroundService.COMMAND to BackgroundCommand.CANCEL)
            .let { startService(it) }
    }

    fun exitApp() {
        stopService(intentFor<BackgroundService>())
        finish()
    }
}
