package suhockii.dev.bookfinder.data.notifier

import suhockii.dev.bookfinder.data.progress.ProgressStep

interface ComponentCommandListener {
    fun onLoadingStep(step: ProgressStep)

    fun onLoadingComplete(statistics: Pair<Int, Int>)

    fun onLoadingCancelled()
}