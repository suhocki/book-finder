package app.suhocki.mybooks.data.notifier

import app.suhocki.mybooks.data.progress.ProgressStep
import javax.inject.Inject

class ComponentNotifier @Inject constructor() {

    private val listeners = mutableListOf<ComponentCommandListener>()
    private var lastProgressStep: ProgressStep? = null

    @Synchronized
    fun addListener(listener: ComponentCommandListener) {
        listeners.add(listener)
    }

    @Synchronized
    fun removeListener(listener: ComponentCommandListener) {
        listeners.remove(listener)
    }

    @Synchronized
    fun onProgressStep(step: ProgressStep) {
        lastProgressStep = step
        listeners.forEach { it.onLoadingStep(step) }
    }

    fun onLoadingComplete(statistics: Pair<Int, Int>) {
        lastProgressStep = null
        listeners.forEach { it.onLoadingComplete(statistics) }
    }

    fun onLoadingCancelled() {
        lastProgressStep = null
        listeners.forEach { it.onLoadingCancelled() }
    }

    fun getLastProgressStep() = lastProgressStep
}