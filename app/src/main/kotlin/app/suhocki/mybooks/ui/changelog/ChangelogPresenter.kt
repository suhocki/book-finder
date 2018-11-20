package app.suhocki.mybooks.ui.changelog

import app.suhocki.mybooks.R
import app.suhocki.mybooks.data.resources.ResourceManager
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.domain.repository.ChangelogRepository
import app.suhocki.mybooks.ui.licenses.entity.HeaderEntity
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

@InjectViewState
class ChangelogPresenter @Inject constructor(
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit,
    private val changelogRepository: ChangelogRepository,
    private val resourceManager: ResourceManager
) : MvpPresenter<ChangelogView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        doAsync(errorReceiver) {
            val data = mutableListOf<Any>()
            data.add(HeaderEntity(resourceManager.getString(R.string.changelog_title)))
            data.addAll(getChangelog())
            uiThread {
                viewState.showChangelog(data)
            }
        }
    }

    private fun getChangelog() =
        changelogRepository.getChangelog()
            .sortedByDescending {it.date}
}