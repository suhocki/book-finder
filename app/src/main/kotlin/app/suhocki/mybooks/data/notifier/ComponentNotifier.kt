package app.suhocki.mybooks.data.notifier

import app.suhocki.mybooks.data.progress.ProgressStep
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

class ComponentNotifier @Inject constructor() {

    private val listeners = mutableListOf<ComponentCommandListener>()
    private var lastProgressStep: AtomicReference<ProgressStep?> = AtomicReference(null)

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
        lastProgressStep.set(step)
        listeners.forEach { it.onLoadingStep(step) }
    }

    @Synchronized
    fun onLoadingComplete(statistics: Pair<Int, Int>) {
        lastProgressStep.set(null)
        listeners.forEach { it.onLoadingComplete(statistics) }
    }

    @Synchronized
    fun onLoadingCancelled() {
        lastProgressStep.set(null)
        listeners.forEach { it.onLoadingCancelled() }
    }

    @Synchronized
    fun getLastProgressStep() = lastProgressStep.get()
}