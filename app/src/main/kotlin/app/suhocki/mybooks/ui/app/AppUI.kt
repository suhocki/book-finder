package app.suhocki.mybooks.ui.app

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.Ids
import org.jetbrains.anko.*

class AppUI : AnkoComponent<Context> {
    lateinit var parent: ViewGroup
    lateinit var debugPanel: LinearLayout

    override fun createView(ui: AnkoContext<Context>) = with(ui) {

        verticalLayout {
            this@AppUI.parent = this

            frameLayout {
                id = Ids.appContainer
            }.lparams(matchParent, 0) {
                weight = 1f
            }

            verticalLayout {
                debugPanel = this
                horizontalPadding = dip(8)
                backgroundColorResource = R.color.colorBlack
            }.lparams(matchParent, wrapContent)
        }
    }
}