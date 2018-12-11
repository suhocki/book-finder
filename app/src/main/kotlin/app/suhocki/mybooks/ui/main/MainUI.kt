package app.suhocki.mybooks.ui.main

import android.content.Context
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.color
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.bottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.navigationView
import org.jetbrains.anko.support.v4.drawerLayout

class MainUI : AnkoComponent<MainActivity> {
    lateinit var drawerLayout: DrawerLayout
    lateinit var parent: ViewGroup
    lateinit var navigationView: NavigationView
    lateinit var bottomBar: AHBottomNavigation
    lateinit var activeConnections: TextView

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {

        verticalLayout {
            this@MainUI.parent = this

            drawerLayout {
                drawerLayout = this
                fitsSystemWindows = false

                coordinatorLayout {

                    frameLayout {
                        id = Ids.mainContainer
                    }.lparams(matchParent, matchParent)

                    bottomNavigation {
                        bottomBar = this
                        id = Ids.bottomMenu
                        AHBottomNavigationAdapter(owner, R.menu.bottom_menu).apply {
                            setupWithBottomNavigation(this@bottomNavigation)
                        }
                        titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
                        defaultBackgroundColor = context.color(R.color.colorPrimary)
                        accentColor = context.color(R.color.colorWhite)
                        inactiveColor = context.color(R.color.colorGray)
                        isBehaviorTranslationEnabled = true
                    }.lparams(matchParent, dimenAttr(R.attr.actionBarSize)) {
                        gravity = Gravity.BOTTOM
                    }

                }.lparams(matchParent, matchParent)

                navigationView {
                    navigationView = this
                    fitsSystemWindows = false

                    object : AnkoComponent<Context> {
                        override fun createView(ui: AnkoContext<Context>): View = with(ui) {
                            frameLayout {
                                backgroundColorResource = R.color.colorPrimary

                                imageView(R.mipmap.ic_launcher_foreground)
                                    .lparams { gravity = Gravity.CENTER }
                            }
                        }
                    }.createView(AnkoContext.create(ctx))
                        .let { addHeaderView(it) }
                }.lparams(wrapContent, matchParent) {
                    gravity = Gravity.START
                }
            }.lparams(matchParent, 0) {
                weight = 1f
            }

            verticalLayout {
                horizontalPadding = dip(8)
                backgroundColorResource = R.color.colorBlack

                textView(resources.getString(R.string.active_connections, 0)) {
                    activeConnections = this
                    textColorResource = R.color.colorGreen
                }

            }.lparams(matchParent, wrapContent)
        }

    }
}