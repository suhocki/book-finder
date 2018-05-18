package app.suhocki.mybooks.data.notifier

import app.suhocki.mybooks.data.progress.ProgressStep

interface ComponentCommandListener {
    fun onLoadingStep(step: ProgressStep)

    fun onLoadingComplete(statistics: Pair<Int, Int>)

    fun onLoadingCancelled()
}