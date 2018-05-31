package app.suhocki.mybooks.data.error

import android.content.Context
import app.suhocki.mybooks.isAppOnForeground
import okhttp3.internal.http2.StreamResetException
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorHandler @Inject constructor(
    private val context: Context
) {
    private val listeners = mutableListOf<ErrorListener>()
    private var lastError: Throwable? = null

    val errorReceiver: (Throwable) -> Unit = {
        if (it !is InterruptedException &&
            it !is InterruptedIOException) {
            it.printStackTrace()
            with(context) {
                if (isAppOnForeground()) {
                    runOnUiThread { toast(it.message.toString()) }
                } else {
                    lastError = it
                }
            }
            val errorType = getErrorType(it)
            listeners.forEach { it.onError(errorType) }
        }
    }

    fun addListener(listener: ErrorListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ErrorListener) {
        listeners.remove(listener)
    }

    private fun getErrorType(throwable: Throwable): ErrorType {
        return when {
            throwable is StreamResetException -> ErrorType.NETWORK

            throwable.cause is UnknownHostException -> ErrorType.NETWORK

            throwable is UnknownHostException -> ErrorType.NETWORK

            throwable.cause is HttpException -> ErrorType.NETWORK

            throwable.cause is SocketTimeoutException -> ErrorType.NETWORK

            throwable is OutOfMemoryError -> ErrorType.OUT_OF_MEMORY

            throwable.cause?.message?.contains(ERROR_MESSAGE_CORRUPTED_FILE) ?: false ->
                ErrorType.CORRUPTED_FILE

            throwable.cause?.message?.contains(ERROR_MESSAGE_INVALID_HOSTNAME) ?: false ->
                ErrorType.NETWORK

            else -> ErrorType.UNKNOWN
        }
    }

    fun invokeLastError() {
        lastError?.let {
            errorReceiver.invoke(it)
            lastError = null
        }
    }

    fun clearLastError() {
        lastError = null
    }

    companion object {
        private const val ERROR_MESSAGE_CORRUPTED_FILE = "zipInputStream.nextEntry must not be null"
        private const val ERROR_MESSAGE_INVALID_HOSTNAME = "No address associated with hostname"
    }
}