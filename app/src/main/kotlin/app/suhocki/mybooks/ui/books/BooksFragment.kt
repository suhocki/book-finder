package app.suhocki.mybooks.ui.books

import android.os.Bundle
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.view.View
import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.data.room.entity.BookDbo
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.extensions.argument
import app.suhocki.mybooks.model.system.debug.DebugPanelController
import app.suhocki.mybooks.ui.base.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.withArguments
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject

class BooksFragment : BaseFragment<BooksUI>(), BooksView {

    @InjectPresenter
    lateinit var presenter: BooksPresenter

    @ProvidePresenter
    fun providePresenter(): BooksPresenter =
        scope.getInstance(BooksPresenter::class.java)

    override val ui by lazy { BooksUI() }

    private val categoryId: String by argument(ARG_CATEGORY_ID)

    @Inject
    lateinit var debugPanelController: DebugPanelController

    private val adapter by lazy {
        BooksAdapter(
            BooksAdapter.BooksDiffCallback(),
            presenter::onBookClick,
            presenter::loadNextPage
        )
    }

    @Suppress("UNCHECKED_CAST")
    override val scopeModuleInstaller = { scope: Scope ->
        val errorReceiver = scope.getInstance(
            Function1::class.java,
            ErrorReceiver::class.java.canonicalName
        ) as (Throwable) -> Unit
        val firestore = scope.getInstance(FirebaseFirestore::class.java)
        val debugPanel = scope.getInstance(DebugPanelController::class.java)

        scope.installModules(
            object : Module() {
                init {
                    bind(FirestoreObserver::class.java)
                        .toInstance(
                            FirestoreObserver(
                                firestore.collection(FirestoreRepository.BOOKS)
                                    .whereEqualTo(BookDbo.CATEGORY_ID, categoryId),
                                errorReceiver,
                                debugPanel::onBooksObserversCount
                            )
                        )
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ui.toolbar.setNavigationOnClickListener {
            val animatedDrawable = ui.toolbar.navigationIcon as DrawerArrowDrawable
            val isArrowVisible = animatedDrawable.progress > 0f

            if (isArrowVisible) {
            } else {
            }
        }

        ui.recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        debugPanelController.showBooksObservers(true)
    }

    override fun onPause() {
        debugPanelController.showBooksObservers(false)
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    override fun showEmptyProgress(show: Boolean) {
        ui.progressBar.visibility =
                if (show) View.VISIBLE
                else View.GONE
    }

    override fun showEmptyError(show: Boolean, error: Throwable?) {
        longToast(error.toString())
    }

    override fun showEmptyView(show: Boolean) {
        longToast("empty data")
    }

    override fun showData(data: List<Any>) {
        postViewAction { adapter.setData(data) }
    }

    override fun showErrorMessage(error: Throwable) {
        longToast(error.toString())
    }

    override fun showRefreshProgress(show: Boolean) {
        TODO("not implemented")
    }

    override fun showPageProgress(show: Boolean) {
        postViewAction { adapter.showProgress(show) }
    }

    companion object {
        private const val ARG_CATEGORY_ID = "arg_category_id"
        fun create(categoryId: String) =
            BooksFragment().apply {
                withArguments(ARG_CATEGORY_ID to categoryId)
            }
    }
}