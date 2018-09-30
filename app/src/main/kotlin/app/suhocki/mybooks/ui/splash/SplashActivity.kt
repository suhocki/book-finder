package app.suhocki.mybooks.ui.splash;

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

    override fun showMainScreen() {
        startActivity<MainActivity>()
        finish()
    }
}