package app.suhocki.mybooks.di.module

import android.content.Context
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.data.remoteconfig.RemoteConfiguration
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.Converters
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.di.provider.ErrorReceiverProvider
import app.suhocki.mybooks.di.provider.MapperConvertersProvider
import app.suhocki.mybooks.di.provider.VersionProvider
import app.suhocki.mybooks.di.provider.ads.InterstitialAdProvider
import app.suhocki.mybooks.domain.model.Version
import app.suhocki.mybooks.model.system.message.SystemMessageNotifier
import app.suhocki.mybooks.ui.base.entity.UploadControlEntity
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import net.grandcentrix.tray.AppPreferences
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module
import java.util.*

class AppModule(
    context: Context,
    locale: Locale
) : Module() {

    init {
        //Global
        bind(Context::class.java).toInstance(context)
        bind(AppPreferences::class.java).toInstance(AppPreferences(context))

        bind(Set::class.java).withName(Converters::class.java)
            .toProvider(MapperConvertersProvider::class.java).providesSingletonInScope()

        bind(ResourceManager::class.java).singletonInScope()
        bind(Version::class.java).toProvider(VersionProvider::class.java).providesSingletonInScope()

        bind(InterstitialAd::class.java).toProvider(InterstitialAdProvider::class.java)
            .providesSingletonInScope()

        bind(Function1::class.java).withName(ErrorReceiver::class.java)
            .toProvider(ErrorReceiverProvider::class.java).singletonInScope()

        bind(RemoteConfiguration::class.java).singletonInScope()
        bind(FirebaseRemoteConfig::class.java).toInstance(FirebaseRemoteConfig.getInstance())
        bind(AdsManager::class.java).singletonInScope()
        bind(UploadControlEntity::class.java).toInstance(UploadControlEntity())
        bind(Locale::class.java).toInstance(locale)

        //Navigation
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)

        bind(SystemMessageNotifier::class.java).toInstance(SystemMessageNotifier())
    }
}