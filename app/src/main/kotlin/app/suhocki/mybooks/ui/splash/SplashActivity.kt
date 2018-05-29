package app.suhocki.mybooks.ui.splash;

import android.os.Bundle
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.ui.initial.InitialActivity
import app.suhocki.mybooks.ui.main.MainActivity
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.startActivity
import toothpick.Toothpick


class SplashActivity : MvpAppCompatActivity(), SplashView {

    @InjectPresenter
    lateinit var presenter: SplashPresenter

    @ProvidePresenter
    fun providePresenter(): SplashPresenter =
        Toothpick.openScope(DI.APP_SCOPE)
            .getInstance(SplashPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.openScopes(DI.APP_SCOPE, DI.SPLASH_ACTIVITY_SCOPE).apply {
            Toothpick.inject(this@SplashActivity, this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Toothpick.closeScope(DI.SPLASH_ACTIVITY_SCOPE)
    }

    override fun showMainScreen() {
        startActivity<MainActivity>()
        finish()
    }

    override fun showInitializationScreen() {
        startActivity<InitialActivity>()
        finish()
    }
}