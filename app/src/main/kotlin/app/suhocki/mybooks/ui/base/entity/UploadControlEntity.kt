package app.suhocki.mybooks.ui.base.entity

import android.os.Parcel
import android.os.Parcelable
import app.suhocki.mybooks.data.notification.ForegroundNotificationHelper
import app.suhocki.mybooks.domain.model.admin.UploadControl
import app.suhocki.mybooks.ui.base.mpeventbus.MPEventBus
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class UploadControlEntity(
    override var fileName: String = "",
    override var stepRes: Int = 0,
    override var progress: Int = 0,
    private var lastUpdateTime: Long = 0
) : UploadControl, AnkoLogger {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fileName)
        parcel.writeInt(stepRes)
        parcel.writeInt(progress)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun sendProgress(
        progress: Int,
        notificationHelper: ForegroundNotificationHelper
    ) {
        if (isEnoughTimePassed()) {
            info { progress }
            this.progress = progress
            MPEventBus.getDefault().postToAll(this)
            notificationHelper.showProgressNotification(stepRes, progress)
        }
    }

    private fun isEnoughTimePassed(): Boolean {
        val current = System.currentTimeMillis()
        return if (current - lastUpdateTime > 500) {
            lastUpdateTime = current
            true
        } else false
    }

    companion object CREATOR : Parcelable.Creator<UploadControlEntity> {
        override fun createFromParcel(parcel: Parcel): UploadControlEntity {
            return UploadControlEntity(parcel)
        }

        override fun newArray(size: Int): Array<UploadControlEntity?> {
            return arrayOfNulls(size)
        }
    }
}
