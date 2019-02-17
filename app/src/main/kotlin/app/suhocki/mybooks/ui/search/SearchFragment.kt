package app.suhocki.mybooks.ui.search

import android.content.Context
import android.view.View
import app.suhocki.mybooks.ui.base.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout

class SearchFragment : BaseFragment<SearchFragment.SearchUI>(), SearchView {

    @InjectPresenter
    lateinit var presenter: SearchPresenter

    @ProvidePresenter
    fun providePresenter(): SearchPresenter =
        scope.getInstance(SearchPresenter::class.java)

    override val ui by lazy {
        SearchUI()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    class SearchUI : AnkoComponent<Context> {
        lateinit var parent: View

        override fun createView(ui: AnkoContext<Context>) =
            ui.frameLayout {
                this@SearchUI.parent = this
            }
    }

}