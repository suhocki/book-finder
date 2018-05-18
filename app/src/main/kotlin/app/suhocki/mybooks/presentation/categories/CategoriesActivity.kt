package app.suhocki.mybooks.presentation.categories

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.notificationManager
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import app.suhocki.mybooks.R
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.di.module.CategoriesActivityModule
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.presentation.books.BooksActivity
import app.suhocki.mybooks.presentation.categories.adapter.CategoriesAdapter
import app.suhocki.mybooks.presentation.categories.adapter.OnCategoryClickListener
import toothpick.Toothpick
import javax.inject.Inject


class CategoriesActivity : MvpAppCompatActivity(), CategoriesView, OnCategoryClickListener {

    @InjectPresenter
    lateinit var presenter: CategoriesPresenter

    @Inject
    lateinit var layout: CategoriesUI

    @Inject
    lateinit var adapter: CategoriesAdapter

    @Inject
    lateinit var layoutManager: LinearLayoutManager

    @ProvidePresenter
    fun providePresenter(): CategoriesPresenter =
        Toothpick.openScope(DI.APP_SCOPE)
            .apply {
                val startFromNotification = intent.extras?.containsKey(ARG_FROM_NOTIFICATION) ?: false
                installModules(CategoriesActivityModule(startFromNotification))
            }
            .getInstance(CategoriesPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scope = Toothpick.openScopes(DI.APP_SCOPE, DI.CATEGORIES_ACTIVITY_SCOPE)
        Toothpick.inject(this@CategoriesActivity, scope)
        title = getString(R.string.categories)
        layout.setContentView(this)
        layout.recyclerView.adapter = adapter
        layout.recyclerView.layoutManager = layoutManager
        adapter.setOnCategoryClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Toothpick.closeScope(DI.CATEGORIES_ACTIVITY_SCOPE)
    }

    override fun showCategories(categories: List<Category>) {
        adapter.submitList(categories)
    }

    override fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }

    override fun onCategoryClick(category: Category) {
        startActivity<BooksActivity>(ARG_CATEGORY to category)
    }

    companion object {
        const val ARG_CATEGORY = "ARG_CATEGORY"
        const val ARG_FROM_NOTIFICATION = "ARG_FROM_NOTIFICATION"
    }
}

