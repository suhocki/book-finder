package app.suhocki.mybooks.data.api.interceptor

import app.suhocki.mybooks.data.notification.ForegroundNotificationHelper
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

internal class ProgressResponseBody(
    private val contentLength: Long?,
    private val responseBody: ResponseBody,
    private val uploadControl: UploadControlEntity,
    private val notificationHelper: ForegroundNotificationHelper
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            val source = responseBody.source()
            bufferedSource = Okio.buffer(source(source))
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                if (bytesRead != -1L) totalBytesRead += bytesRead

                val realContentLength =
                    if (contentLength != null && contentLength > contentLength()) contentLength
                    else contentLength()

                val progress = (totalBytesRead / realContentLength.toDouble() * 100).toInt()
                uploadControl.sendProgress(progress, notificationHelper)
                return bytesRead
            }
        }
    }
}
