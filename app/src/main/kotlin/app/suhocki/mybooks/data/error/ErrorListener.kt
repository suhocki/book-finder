package app.suhocki.mybooks.data.error

interface ErrorListener {
    fun onError(error: ErrorType): Any
}