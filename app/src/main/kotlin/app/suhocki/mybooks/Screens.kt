package app.suhocki.mybooks

import app.suhocki.mybooks.ui.admin.AdminFlowFragment
import app.suhocki.mybooks.ui.admin.AdminFragment
import app.suhocki.mybooks.ui.books.BooksFragment
import app.suhocki.mybooks.ui.catalog.CatalogFlowFragment
import app.suhocki.mybooks.ui.catalog.CatalogFragment
import app.suhocki.mybooks.ui.changelog.ChangelogFragment
import app.suhocki.mybooks.ui.details.DetailsFragment
import app.suhocki.mybooks.ui.drawer.DrawerFlowFragment
import app.suhocki.mybooks.ui.info.InfoFlowFragment
import app.suhocki.mybooks.ui.info.InfoFragment
import app.suhocki.mybooks.ui.licenses.LicensesFragment
import app.suhocki.mybooks.ui.main.MainFlowFragment
import app.suhocki.mybooks.ui.search.SearchFlowFragment
import app.suhocki.mybooks.ui.search.SearchFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {
    object DrawerFlow : SupportAppScreen() {
        override fun getFragment() = DrawerFlowFragment()
    }

    object MainFlow : SupportAppScreen() {
        override fun getFragment() = MainFlowFragment()
    }

    object CatalogFlow : SupportAppScreen() {
        override fun getFragment() = CatalogFlowFragment()
    }

    object Catalog : SupportAppScreen() {
        override fun getFragment() = CatalogFragment()
    }

    data class Books(
        private val categoryId: String
    ) : SupportAppScreen() {
        override fun getFragment() = BooksFragment.create(categoryId)
    }

    object InfoFlow : SupportAppScreen() {
        override fun getFragment() = InfoFlowFragment()
    }

    object Info : SupportAppScreen() {
        override fun getFragment() = InfoFragment()
    }

    object SearchFlow : SupportAppScreen() {
        override fun getFragment() = SearchFlowFragment()
    }

    object Search : SupportAppScreen() {
        override fun getFragment() = SearchFragment()
    }

    object AdminFlow : SupportAppScreen() {
        override fun getFragment() = AdminFlowFragment()
    }

    object Admin : SupportAppScreen() {
        override fun getFragment() = AdminFragment()
    }

    object Licenses : SupportAppScreen() {
        override fun getFragment() = LicensesFragment()
    }

    object Changelog : SupportAppScreen() {
        override fun getFragment() = ChangelogFragment()
    }

    data class Details(
        private val bookId: String
    ) : SupportAppScreen() {
        override fun getFragment() = DetailsFragment.create(bookId)
    }
}