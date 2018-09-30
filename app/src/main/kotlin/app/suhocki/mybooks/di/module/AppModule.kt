package app.suhocki.mybooks.di.module

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import app.suhocki.mybooks.BuildConfig
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.data.database.BooksDatabase
import app.suhocki.mybooks.data.database.RoomRepository
import app.suhocki.mybooks.data.database.dao.*
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.notifier.ComponentNotifier
import app.suhocki.mybooks.data.preferences.SharedPreferencesRepository
import app.suhocki.mybooks.data.progress.ProgressHandler
import app.suhocki.mybooks.data.remoteconfig.RemoteConfiguration
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.DatabaseFileName
import app.suhocki.mybooks.di.SharedPreferencesFileName
import app.suhocki.mybooks.di.provider.SharedPreferencesProvider
import app.suhocki.mybooks.di.provider.VersionProvider
import app.suhocki.mybooks.di.provider.ads.BannerAdProvider
import app.suhocki.mybooks.di.provider.ads.InterstitialAdProvider
import app.suhocki.mybooks.di.provider.database.*
import app.suhocki.mybooks.domain.model.BannerAd
import app.suhocki.mybooks.domain.model.Version
import app.suhocki.mybooks.domain.repository.*
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.jetbrains.anko.configuration
import toothpick.config.Module
import java.util.*

class AppModule(context: Context) : Module() {
    init {
        bind(Context::class.java)
            .toInstance(context)

        bind(ResourceManager::class.java)
            .singletonInScope()

        bind(String::class.java)
            .withName(DatabaseFileName::class.java)
            .toInstance(BuildConfig.DATABASE_FILE_NAME)

        bind(Version::class.java)
            .toProvider(VersionProvider::class.java)
            .providesSingletonInScope()

        bind(String::class.java)
            .withName(SharedPreferencesFileName::class.java)
            .toInstance(BuildConfig.SHARED_PREFERENCES_FILE_NAME)

        bind(BooksDatabase::class.java)
            .toProvider(BooksDatabaseProvider::class.java)
            .providesSingletonInScope()

        bind(BookDao::class.java)
            .toProvider(BookDaoProvider::class.java)
            .providesSingletonInScope()

        bind(BannerDao::class.java)
            .toProvider(BannerDaoProvider::class.java)
            .providesSingletonInScope()

        bind(CategoryDao::class.java)
            .toProvider(CategoryDaoProvider::class.java)
            .providesSingletonInScope()

        bind(StatusStatisticsDao::class.java)
            .toProvider(StatusStatisticsDaoProvider::class.java)
            .providesSingletonInScope()

        bind(YearStatisticsDao::class.java)
            .toProvider(YearStatisticsDaoProvider::class.java)
            .providesSingletonInScope()

        bind(PublisherStatisticsDao::class.java)
            .toProvider(PublisherStatisticsDaoProvider::class.java)
            .providesSingletonInScope()

        bind(AuthorStatisticsDao::class.java)
            .toProvider(AuthorStatisticsDaoProvider::class.java)
            .providesSingletonInScope()

        bind(PriceStatisticsDao::class.java)
            .toProvider(PriceStatisticsDaoProvider::class.java)
            .providesSingletonInScope()

        bind(BooksRepository::class.java)
            .to(RoomRepository::class.java)
            .singletonInScope()

        bind(StatisticsRepository::class.java)
            .to(RoomRepository::class.java)
            .singletonInScope()

        bind(BannersRepository::class.java)
            .to(RoomRepository::class.java)
            .singletonInScope()

        bind(InfoRepository::class.java)
            .to(SharedPreferencesRepository::class.java)
            .singletonInScope()

        bind(SharedPreferences::class.java)
            .toProvider(SharedPreferencesProvider::class.java)
            .providesSingletonInScope()

        bind(InterstitialAd::class.java)
            .toProvider(InterstitialAdProvider::class.java)
            .providesSingletonInScope()

        bind(ErrorHandler::class.java)
            .singletonInScope()

        bind(ProgressHandler::class.java)
            .singletonInScope()

        bind(ComponentNotifier::class.java)
            .singletonInScope()

        bind(SettingsRepository::class.java)
            .to(SharedPreferencesRepository::class.java)
            .singletonInScope()

        bind(RemoteConfiguration::class.java)
            .singletonInScope()

        bind(FirebaseRemoteConfig::class.java)
            .toInstance(FirebaseRemoteConfig.getInstance())

        bind(BannerAd::class.java)
            .toProvider(BannerAdProvider::class.java)
            .providesSingletonInScope()

        bind(AdsManager::class.java)
            .singletonInScope()

        bind(Locale::class.java)
            .toInstance(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    context.configuration.locales.get(0)
                else context.configuration.locale
            )
    }
}