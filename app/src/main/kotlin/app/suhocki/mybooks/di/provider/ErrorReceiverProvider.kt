package app.suhocki.mybooks.di.provider

import app.suhocki.mybooks.data.error.ErrorHandler
import javax.inject.Inject
import javax.inject.Provider

class ErrorReceiverProvider @Inject constructor(
    private val errorHandler: ErrorHandler
) : Provider<(Throwable) -> Unit> {

    override fun get() = { throwable: Throwable ->
        errorHandler.handleError(throwable)
    }
}