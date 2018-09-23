package app.suhocki.mybooks.ui.admin

import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.domain.AdminInteractor
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class AdminPresenter @Inject constructor(
    private val interactor: AdminInteractor,
    private val errorHandler: ErrorHandler
) : MvpPresenter<AdminView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        doAsync(errorHandler.errorReceiver) {
            val files = mutableListOf<Any>()
            files.addAll(interactor.getAvailableFiles())
            uiThread {
                viewState.showData(files)
            }
        }
    }
}