package app.suhocki.mybooks.di.provider.admin

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.model.admin.UploadControl
import javax.inject.Inject
import javax.inject.Provider


class UploadControlProvider @Inject constructor(
    private val resourceManager: ResourceManager
) : Provider<UploadControl> {

    override fun get(): UploadControl = UploadControlEntity()


    inner class UploadControlEntity(
        override var fileName: String = "",

        override var stepRes: Int = resourceManager
            .getStringArrayIdentifiers(R.array.database_upload_steps).first(),

        override var progress: Int = 0
    ) : UploadControl
}