package app.suhocki.mybooks.domain.model.admin

import android.os.Parcelable


interface UploadControl : Parcelable {
    var fileName: String
    var stepRes: Int
    var progress: Int
}