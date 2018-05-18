package app.suhocki.mybooks.presentation.background

import android.support.annotation.StringRes
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import app.suhocki.mybooks.data.progress.ProgressStep

@StateStrategyType(OneExecutionStateStrategy::class)
interface BackgroundView : MvpView {
    fun showLoadingStep(step: ProgressStep)

    fun showSuccess(statistics: Pair<Int, Int>)

    fun stopService()

    fun showError(@StringRes errorDescriptionRes: Int)

    fun showProgress(progressStep: ProgressStep, done: Boolean)
}