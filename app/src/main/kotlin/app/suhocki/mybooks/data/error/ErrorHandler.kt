package app.suhocki.mybooks.data.error

import app.suhocki.mybooks.ui.base.eventbus.ErrorEvent
import app.suhocki.mybooks.ui.base.mpeventbus.MPEventBus
import okhttp3.internal.http2.StreamResetException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

class ErrorHandler @Inject constructor() {

    fun handleError(throwable: Throwable) {
        if (throwable !is InterruptedException &&
            throwable !is InterruptedIOException &&
            throwable !is CancellationException
        ) {
            throwable.printStackTrace()
            val errorType = getErrorType(throwable)
            MPEventBus.getDefault().postToAll(ErrorEvent(errorType.descriptionRes))
        }
    }

    private fun getErrorType(throwable: Throwable): ErrorType {
        return when {
            throwable is ConnectException -> ErrorType.NETWORK

            throwable.cause is HttpException -> ErrorType.NETWORK

            throwable is UnknownHostException -> ErrorType.NETWORK

            throwable is StreamResetException -> ErrorType.NETWORK

            throwable is SSLHandshakeException -> ErrorType.NETWORK

            throwable.cause is ConnectException -> ErrorType.NETWORK

            throwable.cause is UnknownHostException -> ErrorType.NETWORK

            throwable.cause is SSLHandshakeException -> ErrorType.NETWORK

            throwable.cause is SocketTimeoutException -> ErrorType.NETWORK

            throwable is OutOfMemoryError -> ErrorType.OUT_OF_MEMORY

            throwable.cause?.message?.contains(ERROR_MESSAGE_CORRUPTED_FILE) ?: false ->
                ErrorType.CORRUPTED_FILE

            throwable.cause?.message?.contains(ERROR_MESSAGE_INVALID_HOSTNAME) ?: false ->
                ErrorType.NETWORK

            else -> ErrorType.UNKNOWN
        }
    }

    companion object {
        private const val ERROR_MESSAGE_CORRUPTED_FILE = "zipInputStream.nextEntry must not be null"
        private const val ERROR_MESSAGE_INVALID_HOSTNAME = "No address associated with hostname"
    }
}