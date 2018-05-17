package suhockii.dev.bookfinder.data.progress

interface ProgressListener {
    fun onProgress(progressStep: ProgressStep, done: Boolean): Any
}