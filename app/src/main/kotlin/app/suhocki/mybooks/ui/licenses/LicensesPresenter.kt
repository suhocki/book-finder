package app.suhocki.mybooks.ui.licenses

import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.domain.LicensesInteractor
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


@InjectViewState
class LicensesPresenter @Inject constructor(
    private val licensesInteractor: LicensesInteractor,
    private val errorHandler: ErrorHandler
) : MvpPresenter<LicensesView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        doAsync(errorHandler.errorReceiver) {
            val licenses = licensesInteractor.getLicenses()
            uiThread {
                viewState.showLicenses(licenses)
            }
        }
    }
}