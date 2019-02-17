package app.suhocki.mybooks.ui.main

import android.content.Context
import app.suhocki.mybooks.R
import app.suhocki.mybooks.color
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.ahBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import org.jetbrains.anko.*

class MainFlowUI : AnkoComponent<Context> {
    lateinit var bottomBar: AHBottomNavigation

    override fun createView(ui: AnkoContext<Context>) = with(ui) {
        verticalLayout {
            frameLayout {
                id = Ids.mainFlowContainer
            }.lparams(matchParent, 0) {
                weight = 1f
            }

            ahBottomNavigation {
                bottomBar = this
                titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
                defaultBackgroundColor = context.color(R.color.colorPrimary)
                accentColor = context.color(R.color.colorWhite)
                inactiveColor = context.color(R.color.colorGray)
            }.lparams(matchParent, wrapContent)
        }
    }
}