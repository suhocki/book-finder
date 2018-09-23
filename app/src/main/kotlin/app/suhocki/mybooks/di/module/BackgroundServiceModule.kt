package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.data.api.CloudStorageApi
import app.suhocki.mybooks.data.parser.XlsParser
import app.suhocki.mybooks.data.repository.CloudStorageRepositoryImpl
import app.suhocki.mybooks.data.repository.LocalStorageRepository
import app.suhocki.mybooks.di.DatabaseFileUrl
import app.suhocki.mybooks.di.DownloadDirectoryPath
import app.suhocki.mybooks.di.DownloadedFileName
import app.suhocki.mybooks.di.provider.CloudStorageOkHttpProvider
import app.suhocki.mybooks.di.provider.CloudStorageApiProvider
import app.suhocki.mybooks.domain.repository.FileActionsRepository
import app.suhocki.mybooks.domain.repository.CloudStorageRepository
import okhttp3.OkHttpClient
import toothpick.config.Module

class BackgroundServiceModule(downloadDirectory: String) : Module() {
    init {
        bind(String::class.java)
            .withName(DownloadDirectoryPath::class.java)
            .toInstance(downloadDirectory)

        bind(String::class.java)
            .withName(DownloadedFileName::class.java)
            .toInstance(BuildConfig.DOWNLOADED_FILE_NAME)

        bind(String::class.java)
            .withName(DatabaseFileUrl::class.java)
            .toInstance(BuildConfig.DATABASE_FILE_URL)

        bind(OkHttpClient::class.java)
            .toProvider(CloudStorageOkHttpProvider::class.java)
            .providesSingletonInScope()

        bind(CloudStorageApi::class.java)
            .toProvider(CloudStorageApiProvider::class.java)
            .providesSingletonInScope()

        bind(CloudStorageRepository::class.java)
            .to(CloudStorageRepositoryImpl::class.java)
            .singletonInScope()

        bind(FileActionsRepository::class.java)
            .to(LocalStorageRepository::class.java)
            .singletonInScope()

        bind(XlsParser::class.java)
            .singletonInScope()
    }
}