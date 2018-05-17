package suhockii.dev.bookfinder.di.module

import okhttp3.OkHttpClient
import suhockii.dev.bookfinder.BuildConfig
import suhockii.dev.bookfinder.data.network.CloudStorageApi
import suhockii.dev.bookfinder.data.parser.XlsParser
import suhockii.dev.bookfinder.data.repository.GoogleDriveRepository
import suhockii.dev.bookfinder.data.repository.LocalStorageRepository
import suhockii.dev.bookfinder.di.DatabaseFileUrl
import suhockii.dev.bookfinder.di.DownloadDirectoryPath
import suhockii.dev.bookfinder.di.DownloadedFileName
import suhockii.dev.bookfinder.di.provider.network.ApiProvider
import suhockii.dev.bookfinder.di.provider.network.OkHttpClientProvider
import suhockii.dev.bookfinder.domain.repository.FileSystemRepository
import suhockii.dev.bookfinder.domain.repository.ServerRepository
import toothpick.config.Module

class BackgroundServiceModule(
    downloadDirectory: String
) : Module() {
    init {
        bind(String::class.java).withName(DownloadDirectoryPath::class.java).toInstance(downloadDirectory)
        bind(String::class.java).withName(DownloadedFileName::class.java).toInstance(BuildConfig.DOWNLOADED_FILE_NAME)
        bind(String::class.java).withName(DatabaseFileUrl::class.java).toInstance(BuildConfig.DATABASE_FILE_URL)

        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java).providesSingletonInScope()
        bind(CloudStorageApi::class.java).toProvider(ApiProvider::class.java).providesSingletonInScope()

        bind(ServerRepository::class.java).to(GoogleDriveRepository::class.java).singletonInScope()
        bind(FileSystemRepository::class.java).to(LocalStorageRepository::class.java).singletonInScope()
        bind(XlsParser::class.java).singletonInScope()
    }
}