package app.suhocki.mybooks.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Contact
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.info.listener.OnContactClickListener
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.support.v4.ctx
import toothpick.Toothpick

class InfoFragment : BaseFragment(), InfoView, OnContactClickListener {

    private val ui by lazy { InfoUI<InfoFragment>() }

    private val adapter by lazy {
        InfoAdapter(this)
    }

    @InjectPresenter
    lateinit var presenter: InfoPresenter

    @ProvidePresenter
    fun providePresenter(): InfoPresenter =
        Toothpick.openScopes(DI.APP_SCOPE)
            .getInstance(InfoPresenter::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ui.createView(AnkoContext.create(ctx, this@InfoFragment))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ui.recyclerView.adapter = adapter
    }

    override fun showInfoItems(items: MutableList<Any>) {
        adapter.submitList(items)
    }

    override fun onContactClick(contact: Contact) {

    }

    companion object {
        fun newInstance() = InfoFragment()
    }
}