package app.suhocki.mybooks.ui.books

import android.os.Bundle
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.view.View
import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.data.room.entity.BookDbo
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.extensions.argument
import app.suhocki.mybooks.ui.app.AppActivity
import app.suhocki.mybooks.ui.base.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.withArguments
import toothpick.Scope
import toothpick.config.Module

class BooksFragment : BaseFragment<BooksUI>(), BooksView {

    @InjectPresenter
    lateinit var presenter: BooksPresenter

    @ProvidePresenter
    fun providePresenter(): BooksPresenter =
        scope.getInstance(BooksPresenter::class.java)

    override val ui by lazy { BooksUI() }

    private val categoryId: String by argument(ARG_CATEGORY_ID)

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
        val onConnectionsCountChanged =
            (activity as AppActivity)::onFirestoreConnectionsCount
        val firestore = scope.getInstance(FirebaseFirestore::class.java)

        scope.installModules(
            object : Module() {
                init {
                    //region firestore books
                    bind(FirestoreObserver::class.java)
                        .toInstance(
                            FirestoreObserver(
                                firestore.collection(FirestoreRepository.BOOKS)
                                    .whereEqualTo(BookDbo.CATEGORY_ID, categoryId),
                                errorReceiver,
                                onConnectionsCountChanged
                            )
                        )
                    //endregion
                }
            }
        )
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