package app.suhocki.mybooks.ui.admin.entity

import app.suhocki.mybooks.domain.model.admin.UploadControl

class UploadControlEntity(
    override var fileName: String,
    override var stepRes: Int,
    override var progress: Int
) : UploadControl {
    constructor(uploadControl: UploadControl) : this(
        uploadControl.fileName,
        uploadControl.stepRes,
        uploadControl.progress
    )
}