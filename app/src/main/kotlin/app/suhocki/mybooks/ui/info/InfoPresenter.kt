package app.suhocki.mybooks.ui.info

import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.domain.InfoInteractor
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class InfoPresenter @Inject constructor(
    private val infoInteractor: InfoInteractor,
    private val errorHandler: ErrorHandler
) : MvpPresenter<InfoView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        doAsync(errorHandler.errorReceiver) {
            val items = mutableListOf<Any>().apply {
                add(infoInteractor.getHeaderOrganization())
                addAll(infoInteractor.getContacts())
            }
            uiThread {
                viewState.showInfoItems(items)
            }
        }
    }
}