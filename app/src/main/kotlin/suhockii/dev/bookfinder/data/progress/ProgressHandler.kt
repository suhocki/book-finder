package suhockii.dev.bookfinder.data.progress

import javax.inject.Inject

class ProgressHandler @Inject constructor() {
    private val listeners = mutableMapOf<ProgressListener, UpdateRule>()

    fun addListener(listener: ProgressListener, updateInterval: Long) {
        listeners[listener] = UpdateRule(updateInterval)
    }

    fun removeListener(listener: ProgressListener) {
        listeners.remove(listener)
    }

    fun onProgress(progressStep: ProgressStep, done: Boolean) {
        listeners.forEach { (listener, updateRule) ->
            if (updateRule.requestUpdate() || done) listener.onProgress(progressStep, done)
        }
    }
}
