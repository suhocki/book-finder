package app.suhocki.mybooks.data.notifier

import app.suhocki.mybooks.data.progress.ProgressStep
import javax.inject.Inject

class ComponentNotifier @Inject constructor() {

    private val listeners = mutableListOf<ComponentCommandListener>()

    fun addListener(listener: ComponentCommandListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ComponentCommandListener) {
        listeners.remove(listener)
    }

    fun onLoadingStep(step: ProgressStep) {
        listeners.forEach { it.onLoadingStep(step) }
    }

    fun onLoadingComplete(statistics: Pair<Int, Int>) {
        listeners.forEach { it.onLoadingComplete(statistics) }
    }

    fun onLoadingCancelled() {
        listeners.forEach { it.onLoadingCancelled() }
    }
}