package app.suhocki.mybooks.di.module

import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.data.remoteconfig.RemoteConfig
import app.suhocki.mybooks.di.provider.VersionProvider
import app.suhocki.mybooks.di.provider.ads.InterstitialAdProvider
import app.suhocki.mybooks.domain.model.Version
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module

class UiModule : Module() {
    init {
        //Ui entities
        bind(Version::class.java).toProvider(VersionProvider::class.java).providesSingletonInScope()

        //Remote control dependencies
        bind(RemoteConfig::class.java).toInstance(RemoteConfig(FirebaseRemoteConfig.getInstance()))

        //Ads dependencies
        bind(InterstitialAd::class.java).toProvider(InterstitialAdProvider::class.java).providesSingletonInScope()
        bind(AdsManager::class.java).singletonInScope()

        //Navigation
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
    }
}