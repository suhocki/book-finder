package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.data.api.FilesApi
import app.suhocki.mybooks.di.DownloadDirectoryPath
import app.suhocki.mybooks.di.provider.FilesApiProvider
import app.suhocki.mybooks.di.provider.FilesOkHttpProvider
import okhttp3.OkHttpClient
import toothpick.config.Module

class UploadServiceModule(
    downloadDirectoryPath: String
) : Module() {
    init {
        bind(OkHttpClient::class.java)
            .toProvider(FilesOkHttpProvider::class.java)
            .providesSingletonInScope()

        bind(FilesApi::class.java)
            .toProvider(FilesApiProvider::class.java)
            .providesSingletonInScope()

        bind(String::class.java)
            .withName(DownloadDirectoryPath::class.java)
            .toInstance(downloadDirectoryPath)
    }
}