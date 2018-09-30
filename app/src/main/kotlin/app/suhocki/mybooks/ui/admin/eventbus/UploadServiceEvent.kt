package app.suhocki.mybooks.ui.admin.eventbus

import app.suhocki.mybooks.domain.model.admin.UploadControl

data class UploadServiceEvent(
    val uploadControl: UploadControl?
)