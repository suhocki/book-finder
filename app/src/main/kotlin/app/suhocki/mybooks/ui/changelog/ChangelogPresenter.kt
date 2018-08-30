package app.suhocki.mybooks.ui.changelog

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.error.ErrorHandler
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.domain.ChangelogInteractor
import app.suhocki.mybooks.ui.licenses.entity.HeaderEntity
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class ChangelogPresenter @Inject constructor(
    private val changelogInteractor: ChangelogInteractor,
    private val errorHandler: ErrorHandler,
    private val resourceManager: ResourceManager
) : MvpPresenter<ChangelogView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        doAsync(errorHandler.errorReceiver) {
            val data = mutableListOf<Any>()
            data.add(HeaderEntity(resourceManager.getString(R.string.changelog_title)))
            data.addAll(changelogInteractor.getChangelog())
            uiThread {
                viewState.showChangelog(data)
            }
        }
    }
}