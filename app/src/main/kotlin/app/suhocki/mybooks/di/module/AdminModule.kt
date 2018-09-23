package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.data.googledrive.GoogleDriveRepositoryImpl
import app.suhocki.mybooks.data.api.GoogleDriveApi
import app.suhocki.mybooks.di.provider.GoogleDriveApiProvider
import app.suhocki.mybooks.di.provider.GoogleDriveOkHttpProvider
import app.suhocki.mybooks.domain.repository.GoogleDriveRepository
import okhttp3.OkHttpClient
import toothpick.config.Module

class AdminModule : Module() {
    init {
        bind(OkHttpClient::class.java)
            .toProvider(GoogleDriveOkHttpProvider::class.java)
            .providesSingletonInScope()

        bind(GoogleDriveApi::class.java)
            .toProvider(GoogleDriveApiProvider::class.java)
            .providesSingletonInScope()

        bind(GoogleDriveRepository::class.java)
            .to(GoogleDriveRepositoryImpl::class.java)
            .singletonInScope()
    }
}