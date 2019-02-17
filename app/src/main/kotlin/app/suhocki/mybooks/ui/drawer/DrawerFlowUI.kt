package app.suhocki.mybooks.ui.drawer

import android.content.Context
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import app.suhocki.mybooks.ui.Ids
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.drawerLayout

class DrawerFlowUI : AnkoComponent<Context> {
    lateinit var drawerLayout: DrawerLayout

    override fun createView(ui: AnkoContext<Context>) = with(ui) {
        drawerLayout {
            drawerLayout = this

            frameLayout {
                id = Ids.drawerMainContainer
            }.lparams(matchParent, matchParent)

            frameLayout {
                id = Ids.drawerMenuContainer
            }.lparams(dip(300), matchParent) {
                gravity = Gravity.START
            }
        }
    }
}