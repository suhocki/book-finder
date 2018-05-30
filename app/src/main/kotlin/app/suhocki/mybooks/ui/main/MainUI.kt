package app.suhocki.mybooks.ui.main

import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import app.suhocki.mybooks.R
import app.suhocki.mybooks.color
import app.suhocki.mybooks.inLandscape
import app.suhocki.mybooks.ui.base.bottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.navigationView
import org.jetbrains.anko.support.v4.drawerLayout

class MainUI : AnkoComponent<MainActivity> {
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var bottomBar: AHBottomNavigation

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {

        drawerLayout {
            drawerLayout = this
            fitsSystemWindows = false

            coordinatorLayout {

                frameLayout {
                    id = R.id.id_main_container
                }.lparams(matchParent, matchParent)

                bottomNavigation {
                    bottomBar = this
                    id = R.id.id_bottom_menu
                    AHBottomNavigationAdapter(owner, R.menu.app_menu).apply {
                        setupWithBottomNavigation(this@bottomNavigation)
                    }
                    titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
                    defaultBackgroundColor = context.color(R.color.colorPrimary)
                    accentColor = context.color(R.color.colorWhite)
                    inactiveColor = context.color(R.color.colorGray)
                    isBehaviorTranslationEnabled = true
                    owner.inLandscape {
                        titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
                    }
                }.lparams(matchParent, dimenAttr(R.attr.actionBarSize)) {
                    gravity = Gravity.BOTTOM
                }

            }.lparams(matchParent, matchParent)

            navigationView {
                navigationView = this
                fitsSystemWindows = false
                inflateMenu(R.menu.app_menu)
            }.lparams(wrapContent, matchParent) {
                gravity = Gravity.START
            }
        }
    }
}