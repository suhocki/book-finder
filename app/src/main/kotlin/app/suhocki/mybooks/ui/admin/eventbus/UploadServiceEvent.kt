package app.suhocki.mybooks.ui.admin.eventbus

import android.os.Parcel
import android.os.Parcelable
import app.suhocki.mybooks.domain.model.admin.UploadControl

data class UploadServiceEvent(
    val uploadControl: UploadControl?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(UploadControl::class.java.classLoader) as UploadControl
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(uploadControl, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UploadServiceEvent> {
        override fun createFromParcel(parcel: Parcel): UploadServiceEvent {
            return UploadServiceEvent(parcel)
        }

        override fun newArray(size: Int): Array<UploadServiceEvent?> {
            return arrayOfNulls(size)
        }
    }
}