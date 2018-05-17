package suhockii.dev.bookfinder.presentation.initial

import android.support.annotation.StringRes
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import suhockii.dev.bookfinder.data.progress.ProgressStep

@StateStrategyType(OneExecutionStateStrategy::class)
interface InitialView : MvpView {
    fun showLoadingStep(step: ProgressStep)

    fun showSuccess(statistics: Pair<Int, Int>)

    fun showMainScreen()

    fun showInitialState()

    fun showError(@StringRes errorDescriptionRes: Int)

    fun showProgress(progressStep: ProgressStep, done: Boolean)

    fun synchronizeWithBackground()

    fun initBackgroundService()
}