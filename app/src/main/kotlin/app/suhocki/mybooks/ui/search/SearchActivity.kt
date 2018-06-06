package app.suhocki.mybooks.ui.search

import android.os.Bundle
import android.view.View
import android.view.Window
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.SearchKey
import app.suhocki.mybooks.domain.model.filter.FilterAuthor
import app.suhocki.mybooks.domain.model.filter.FilterPublisher
import app.suhocki.mybooks.ui.base.listener.OnFilterAuthorClickListener
import app.suhocki.mybooks.ui.base.listener.OnFilterPublisherClickListener
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.setContentView
import toothpick.Toothpick
import toothpick.config.Module

class SearchActivity : MvpAppCompatActivity(), SearchView,
    OnFilterAuthorClickListener,
    OnFilterPublisherClickListener {

    private val ui by lazy { SearchUI() }

    private val adapter by lazy { SearchAdapter(this, this) }

    @InjectPresenter
    lateinit var presenter: SearchPresenter

    @ProvidePresenter
    fun providePresenter(): SearchPresenter {
        val scope = Toothpick
            .openScopes(DI.APP_SCOPE, DI.BOOKS_ACTIVITY_SCOPE, DI.SEARCH_ACTIVITY_SCOPE)
        val searchKey = intent.extras[ARG_SEARCH_KEY] as String
        scope.installModules(object : Module() {
            init {
                bind(String::class.java)
                    .withName(SearchKey::class.java)
                    .toInstance(searchKey)
            }
        })
        return scope.getInstance(SearchPresenter::class.java).also {
            Toothpick.closeScope(DI.SEARCH_ACTIVITY_SCOPE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        ui.setContentView(this)
        ui.recyclerView.adapter = adapter
    }

    override fun showTitleRes(title: Int) {
        supportActionBar!!.setTitle(title)
    }

    override fun showSearchItems(searchItems: List<Any>) {
        adapter.submitList(searchItems)
    }

    override fun showProgressBar(isVisible: Boolean) {
        ui.progressBar.visibility = View.GONE
    }

    override fun showEmptyScreen() {
        ui.emptyView.visibility = View.VISIBLE
    }

    override fun onFilterPublisherClick(filterPublisher: FilterPublisher) {

    }

    override fun onFilterAuthorClick(filterAuthor: FilterAuthor) {

    }

    companion object {
        const val ARG_SEARCH_KEY = "ARG_SEARCH_KEY"
    }
}