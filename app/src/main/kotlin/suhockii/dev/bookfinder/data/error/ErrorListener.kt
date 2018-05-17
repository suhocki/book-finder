package suhockii.dev.bookfinder.data.error

interface ErrorListener {
    fun onError(error: ErrorType): Any
}