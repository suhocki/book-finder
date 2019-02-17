package app.suhocki.mybooks.ui.details

import app.suhocki.mybooks.data.mapper.Mapper
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.model.system.flow.FlowRouter
import app.suhocki.mybooks.ui.base.entity.UiBook
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import javax.inject.Inject

@InjectViewState
class DetailsPresenter @Inject constructor(
    private val firestoreQuery: Query,
    private val router: FlowRouter,
    private val mapper: Mapper,
    @ErrorReceiver private val errorReceiver: (Throwable) -> Unit
) : MvpPresenter<DetailsView>() {

    private lateinit var observer: ListenerRegistration

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observer = firestoreQuery.addSnapshotListener { snapshot, exception ->
            if (snapshot == null) {
                errorReceiver.invoke(exception!!)
            } else {
                val book = mapper.map<UiBook>(snapshot.documents.first())
                viewState.showBook(book)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        observer.remove()
    }

    fun onBackPressed() = router.exit()
}
