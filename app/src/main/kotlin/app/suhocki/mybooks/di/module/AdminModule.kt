package app.suhocki.mybooks.di.module

import android.content.Context
import app.suhocki.mybooks.data.api.GoogleDriveApi
import app.suhocki.mybooks.data.googledrive.GoogleDriveRepositoryImpl
import app.suhocki.mybooks.data.localstorage.LocalStorageRepository
import app.suhocki.mybooks.data.parser.XlsParser
import app.suhocki.mybooks.di.DownloadDirectoryPath
import app.suhocki.mybooks.di.provider.GoogleDriveApiProvider
import app.suhocki.mybooks.di.provider.GoogleDriveOkHttpProvider
import app.suhocki.mybooks.di.provider.admin.UploadControlProvider
import app.suhocki.mybooks.domain.model.admin.UploadControl
import app.suhocki.mybooks.domain.repository.FileActionsRepository
import app.suhocki.mybooks.domain.repository.GoogleDriveRepository
import okhttp3.OkHttpClient
import toothpick.config.Module

class AdminModule(
    context: Context,
    downloadDirectory: String
) : Module() {

    init {
        bind(Context::class.java)
            .toInstance(context)

        bind(OkHttpClient::class.java)
            .toProvider(GoogleDriveOkHttpProvider::class.java)
            .providesSingletonInScope()

        bind(GoogleDriveApi::class.java)
            .toProvider(GoogleDriveApiProvider::class.java)
            .providesSingletonInScope()

        bind(GoogleDriveRepository::class.java)
            .to(GoogleDriveRepositoryImpl::class.java)
            .singletonInScope()

        bind(UploadControl::class.java)
            .toProvider(UploadControlProvider::class.java)
            .singletonInScope()

        bind(String::class.java)
            .withName(DownloadDirectoryPath::class.java)
            .toInstance(downloadDirectory)

        bind(FileActionsRepository::class.java)
            .to(LocalStorageRepository::class.java)
            .singletonInScope()

        bind(XlsParser::class.java)
            .singletonInScope()
    }
}