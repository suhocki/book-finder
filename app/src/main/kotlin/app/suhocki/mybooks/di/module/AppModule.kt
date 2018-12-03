package app.suhocki.mybooks.di.module

import android.os.Build
import app.suhocki.mybooks.App
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.data.context.ContextManager
import app.suhocki.mybooks.data.preferences.AppPreferencesRepository
import app.suhocki.mybooks.data.remoteconfig.RemoteConfiguration
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.di.Converters
import app.suhocki.mybooks.di.provider.AppPreferencesProvider
import app.suhocki.mybooks.di.provider.ErrorReceiverProvider
import app.suhocki.mybooks.di.provider.MapperConvertersProvider
import app.suhocki.mybooks.di.provider.VersionProvider
import app.suhocki.mybooks.di.provider.ads.InterstitialAdProvider
import app.suhocki.mybooks.domain.model.Version
import app.suhocki.mybooks.domain.repository.SettingsRepository
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import net.grandcentrix.tray.AppPreferences
import org.jetbrains.anko.configuration
import toothpick.config.Module
import java.util.*

class AppModule(app: App) : Module() {
    init {
        bind(App::class.java)
            .toInstance(app)

        bind(Set::class.java)
            .withName(Converters::class.java)
            .toProvider(MapperConvertersProvider::class.java)
            .providesSingletonInScope()

        bind(ContextManager::class.java)
            .singletonInScope()

        bind(ResourceManager::class.java)
            .singletonInScope()

        bind(Version::class.java)
            .toProvider(VersionProvider::class.java)
            .providesSingletonInScope()

        bind(AppPreferences::class.java)
            .toProvider(AppPreferencesProvider::class.java)
            .providesSingletonInScope()

        bind(InterstitialAd::class.java)
            .toProvider(InterstitialAdProvider::class.java)
            .providesSingletonInScope()

        bind(Function1::class.java)
            .withName(ErrorReceiver::class.java)
            .toProvider(ErrorReceiverProvider::class.java)
            .singletonInScope()

        bind(SettingsRepository::class.java)
            .to(AppPreferencesRepository::class.java)
            .singletonInScope()

        bind(RemoteConfiguration::class.java)
            .singletonInScope()

        bind(FirebaseRemoteConfig::class.java)
            .toInstance(FirebaseRemoteConfig.getInstance())

        bind(AdsManager::class.java)
            .singletonInScope()

        bind(UploadControlEntity::class.java)
            .toInstance(UploadControlEntity())

        @Suppress("DEPRECATION")
        bind(Locale::class.java)
            .toInstance(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    app.applicationContext.configuration.locales.get(0)
                else app.applicationContext.configuration.locale
            )
    }
}