package app.suhocki.mybooks.ui.base.eventbus

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.StringRes

data class ErrorEvent(
    @StringRes val messageRes: Int
): Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(messageRes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ErrorEvent> {
        override fun createFromParcel(parcel: Parcel): ErrorEvent {
            return ErrorEvent(parcel)
        }

        override fun newArray(size: Int): Array<ErrorEvent?> {
            return arrayOfNulls(size)
        }
    }
}