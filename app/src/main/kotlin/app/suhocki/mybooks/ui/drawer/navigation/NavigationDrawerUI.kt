package app.suhocki.mybooks.ui.drawer.navigation

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.Ids
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView

class NavigationDrawerUI : AnkoComponent<Context> {
    lateinit var recyclerView: RecyclerView

    override fun createView(ui: AnkoContext<Context>) = with(ui) {
        frameLayout {
            backgroundColorResource = R.color.colorWhite

            recyclerView {
                recyclerView = this
                id = Ids.recyclerDrawerMenu
                clipToPadding = false
                layoutManager = LinearLayoutManager(context)
            }
        }
    }
}