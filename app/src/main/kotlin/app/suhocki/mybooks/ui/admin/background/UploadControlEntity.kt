package app.suhocki.mybooks.ui.admin.background

import android.os.Parcel
import android.os.Parcelable
import app.suhocki.mybooks.domain.model.admin.UploadControl

class UploadControlEntity(
    override var fileName: String = "",
    override var stepRes: Int,
    override var progress: Int = 0
) : UploadControl {

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

    companion object CREATOR : Parcelable.Creator<UploadControlEntity> {
        override fun createFromParcel(parcel: Parcel): UploadControlEntity {
            return UploadControlEntity(parcel)
        }

        override fun newArray(size: Int): Array<UploadControlEntity?> {
            return arrayOfNulls(size)
        }
    }
}