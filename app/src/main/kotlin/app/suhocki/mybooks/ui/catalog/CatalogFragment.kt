package app.suhocki.mybooks.ui.catalog

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.RecyclerView
import android.view.View
import app.suhocki.mybooks.Analytics
import app.suhocki.mybooks.data.firestore.FirestoreObserver
import app.suhocki.mybooks.data.firestore.FirestoreRepository
import app.suhocki.mybooks.di.BannersObserver
import app.suhocki.mybooks.di.CategoriesObserver
import app.suhocki.mybooks.di.ErrorReceiver
import app.suhocki.mybooks.domain.model.Book
import app.suhocki.mybooks.openLink
import app.suhocki.mybooks.presentation.global.GlobalMenuController
import app.suhocki.mybooks.ui.app.AppActivity
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.entity.UiBook
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.support.v4.longToast
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject


class CatalogFragment : BaseFragment<CatalogUI>(), CatalogView {

    @InjectPresenter
    lateinit var presenter: CatalogPresenter

    @ProvidePresenter
    fun providePresenter(): CatalogPresenter =
        scope.getInstance(CatalogPresenter::class.java)

    @Inject
    lateinit var menuController: GlobalMenuController

    override val ui by lazy { CatalogUI() }

    private val adapter by lazy {
        CatalogAdapter(
            CatalogAdapter.CatalogDiffCallback(),
            presenter::onCategoryClick,
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
                    //region firestore banners
                    bind(FirestoreObserver::class.java)
                        .withName(BannersObserver::class.java)
                        .toInstance(
                            FirestoreObserver(
                                firestore.collection(FirestoreRepository.BANNERS),
                                errorReceiver,
                                onConnectionsCountChanged
                            )
                        )
                    //endregion

                    //region firestore categories
                    bind(FirestoreObserver::class.java)
                        .withName(CategoriesObserver::class.java)
                        .toInstance(
                            FirestoreObserver(
                                firestore.collection(FirestoreRepository.CATEGORIES),
                                errorReceiver,
                                onConnectionsCountChanged
                            )
                        )
                    //endregion
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
                menuController.open()
            }
        }

        ui.recyclerView.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    private fun animateToolbarNavigationButton(toArrow: Boolean) {
        val animatedDrawable = ui.toolbar.navigationIcon as DrawerArrowDrawable

        val fromProgress = animatedDrawable.progress
        val toProgress = if (toArrow) 1f else 0f

        ObjectAnimator.ofFloat(animatedDrawable, "progress", fromProgress, toProgress)
            .start()
    }

    override fun showRecyclerDecoration(decoration: RecyclerView.ItemDecoration) {
        ui.recyclerView.apply {
            while (itemDecorationCount > 0) removeItemDecorationAt(0)
            addItemDecoration(decoration)
        }
    }

    override fun showBuyDrawableForItem(book: UiBook, @DrawableRes drawableRes: Int) {
        val indexOfBook = adapter.items.indexOf(book)
        adapter.notifyItemChanged(indexOfBook, drawableRes)
    }

    override fun openBookWebsite(book: Book) {
        Analytics.bookAddedToCart(book)
        context!!.openLink(book.website)
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
        const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"
        const val SEARCH_RESULT_POSITION = 3
    }
}

