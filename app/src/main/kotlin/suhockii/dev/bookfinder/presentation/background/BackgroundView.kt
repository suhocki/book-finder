package suhockii.dev.bookfinder.presentation.background

import android.support.annotation.StringRes
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import suhockii.dev.bookfinder.data.progress.ProgressStep

@StateStrategyType(OneExecutionStateStrategy::class)
interface BackgroundView : MvpView {
    fun showLoadingStep(step: ProgressStep)

    fun showSuccess(statistics: Pair<Int, Int>)

    fun cancelService()

    fun showError(@StringRes errorDescriptionRes: Int)

    fun showProgress(progressStep: ProgressStep, done: Boolean)
}