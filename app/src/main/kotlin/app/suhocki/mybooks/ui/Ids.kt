package app.suhocki.mybooks.ui

import android.support.annotation.IdRes
import android.view.View
import app.suhocki.mybooks.R

object Ids {
    //region recycler view
    @IdRes
    val recyclerBanners = View.generateViewId()
    @IdRes
    val recyclerCatalog = View.generateViewId()
    @IdRes
    val recyclerInfo = View.generateViewId()
    @IdRes
    val recyclerDrawerMenu = View.generateViewId()
    @IdRes
    val recyclerBooks = View.generateViewId()
    @IdRes
    val recyclerFilter = View.generateViewId()
    @IdRes
    val appbarDetails = View.generateViewId()
    @IdRes
    val fab = View.generateViewId()
    @IdRes
    val recyclerLicenses = View.generateViewId()
    @IdRes
    val recyclerChangelog = View.generateViewId()
    //endregion

    //Fragment containers
    @IdRes
    val appContainer = View.generateViewId()
    @IdRes
    val drawerMainContainer = View.generateViewId()
    @IdRes
    val mainFlowContainer = View.generateViewId()
    @IdRes
    val drawerMenuContainer = View.generateViewId()
    @IdRes
    val flowFragmentContainer = View.generateViewId()

    @IdRes
    val bottomMenu = View.generateViewId()
    @IdRes
    val search = View.generateViewId()
    @IdRes
    val recyclerSearch = View.generateViewId()
    @IdRes
    val filterContainer = View.generateViewId()

    /*Navigation menu*/
    @IdRes
    val navCatalog = R.id.nav_catalog
    @IdRes
    val navSearch = R.id.nav_search
    @IdRes
    val navInfo = R.id.nav_info
    @IdRes
    val navAdmin = View.generateViewId()
    @IdRes
    val navLicenses = View.generateViewId()
    @IdRes
    val navChanges = View.generateViewId()
    @IdRes
    val navAboutDeveloper = View.generateViewId()
}