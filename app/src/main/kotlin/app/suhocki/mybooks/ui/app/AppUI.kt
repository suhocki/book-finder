package app.suhocki.mybooks.ui.app

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.Ids
import org.jetbrains.anko.*

class AppUI : AnkoComponent<Context> {
    lateinit var parent: ViewGroup
    lateinit var debugPanel: View
    lateinit var firestoreConnections: TextView

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

                textView(resources.getString(R.string.firestore_connection, 0)) {
                    firestoreConnections = this
                    textColorResource = R.color.colorGreen
                }

            }.lparams(matchParent, wrapContent)
        }
    }
}