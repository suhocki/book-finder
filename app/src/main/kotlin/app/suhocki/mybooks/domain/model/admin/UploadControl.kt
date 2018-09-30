package app.suhocki.mybooks.domain.model.admin


interface UploadControl {
    var fileName: String
    var stepRes: Int
    var progress: Int
}