package app.suhocki.mybooks.ui.licenses

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.domain.repository.LicenseRepository
import app.suhocki.mybooks.model.system.flow.FlowRouter
import app.suhocki.mybooks.ui.licenses.entity.HeaderEntity
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject


@InjectViewState
class LicensesPresenter @Inject constructor(
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    private val licensesRepository: LicenseRepository,
    private val resourceManager: ResourceManager,
    private val router: FlowRouter
) : MvpPresenter<LicensesView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        doAsync(errorReceiver) {
            val header = HeaderEntity(resourceManager.getString(R.string.licenses_title))
            val data = mutableListOf<Any>()
            data.add(header)
            data.addAll(licensesRepository.getLicenses())
            uiThread {
                viewState.showData(data)
            }
        }
    }

    fun onBackPressed() = router.exit()
}