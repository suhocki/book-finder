package app.suhocki.mybooks.presentation.initial

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.progress.ProgressStep
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.presentation.background.BackgroundCommand
import app.suhocki.mybooks.presentation.background.BackgroundService
import app.suhocki.mybooks.presentation.main.MainActivity
import app.suhocki.mybooks.setGone
import app.suhocki.mybooks.setVisible
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.*
import toothpick.Toothpick
import javax.inject.Inject


class InitialActivity : MvpAppCompatActivity(), InitialView {

    @InjectPresenter
    lateinit var presenter: InitialPresenter

    @Inject
    lateinit var ui: InitialUI

    @ProvidePresenter
    fun providePresenter(): InitialPresenter =
        Toothpick.openScopes(DI.APP_SCOPE)
            .getInstance(InitialPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.openScopes(DI.APP_SCOPE).apply {
            Toothpick.inject(this@InitialActivity, this)
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        ui.setContentView(this)
    }

    override fun onStart() {
        super.onStart()
        startService<BackgroundService>(
            BackgroundService.COMMAND to BackgroundCommand.STOP_FOREGROUND
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Toothpick.closeScope(DI.INITIAL_ACTIVITY_SCOPE)
    }

    override fun showLoadingStep(step: ProgressStep) {
        ui.textTitle.text =
                getString(R.string.step_info, step.number, ProgressStep.values().size)
        when (step) {
            ProgressStep.DOWNLOADING -> showLoading()

            ProgressStep.UNZIPPING -> showUnzipping()

            ProgressStep.ANALYZING -> showAnalyzing()

            ProgressStep.PARSING -> showParsing()

            ProgressStep.SAVING -> showSaving()
        }
    }

    override fun showProgress(progressStep: ProgressStep, done: Boolean) = with(ui) {
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

    private fun showLoading() = with(ui) {
        ivTop.setImageResource(R.drawable.logo)
        textProgress.text = getString(R.string.percent, 0)
        textDescription.textResource = R.string.downloading
        setVisible(btnInBackground, textProgress, progressBar, btnCancel)
        setGone(btnExit, btnRetry, btnDownload, btnContinue)
    }

    private fun showUnzipping() = with(ui) {
        textDescription.textResource = R.string.unzipping
        setVisible(btnInBackground, progressBar, btnCancel)
        setGone(textProgress, btnExit, btnRetry, btnDownload, btnContinue)
    }

    private fun showAnalyzing() = with(ui) {
        ui.textDescription.textResource = R.string.analyzing
        setVisible(btnInBackground, progressBar, btnCancel)
        setGone(textProgress, btnExit, btnRetry, btnDownload, btnContinue)
    }

    private fun showParsing() = with(ui) {
        ui.textDescription.textResource = R.string.parsing
        setVisible(btnInBackground, textProgress, progressBar, btnCancel)
        setGone(btnExit, btnRetry, btnDownload, btnContinue)
    }

    private fun showSaving() = with(ui) {
        ui.textDescription.textResource = R.string.saving
        setVisible(btnInBackground, progressBar, btnCancel)
        setGone(textProgress, btnExit, btnRetry, btnDownload, btnContinue)
    }

    override fun showSuccess(statistics: Pair<Int, Int>) = with(ui) {
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
        startActivity<MainActivity>()
        finish()
    }

    override fun showInitialState() = with(ui) {
        textTitle.text = getString(R.string.info)
        textDescription.textResource = R.string.database_load_need
        setVisible(btnDownload, btnExit)
        setGone(btnInBackground, btnCancel, progressBar, textProgress, btnRetry)
    }

    override fun showError(errorDescriptionRes: Int) = with(ui) {
        textTitle.textResource = R.string.error
        ivTop.setImageResource(R.drawable.ic_error)
        textDescription.textResource = errorDescriptionRes
        setVisible(btnRetry, btnExit)
        setGone(btnDownload, btnInBackground, btnCancel, progressBar, textProgress)
    }

    override fun synchronizeWithBackground() {
        startService<BackgroundService>(
            BackgroundService.COMMAND to BackgroundCommand.SYNC_STATE
        )
    }

    fun startDownloading() {
        startService<BackgroundService>(
            BackgroundService.COMMAND to BackgroundCommand.START
        )
    }

    fun cancelDownloading() {
        startService<BackgroundService>(
            BackgroundService.COMMAND to BackgroundCommand.CANCEL
        )
    }

    override fun exitApp() {
        stopService<BackgroundService>()
        finish()
    }

    override fun showToast(messageRes: Int) {
        toast(messageRes)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}
