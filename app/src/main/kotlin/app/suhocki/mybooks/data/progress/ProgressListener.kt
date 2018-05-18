package app.suhocki.mybooks.data.progress

interface ProgressListener {
    fun onProgress(progressStep: ProgressStep, done: Boolean): Any
}