package app.suhocki.mybooks.ui.main

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import app.suhocki.mybooks.R
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
                accentColor = ContextCompat.getColor(context, R.color.colorWhite)
                inactiveColor = ContextCompat.getColor(context, R.color.colorGray)
                defaultBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
                setUseElevation(false)
                ViewCompat.setElevation(this, 50f)
                ViewCompat.setTranslationZ(this, 50f)
            }.lparams(matchParent, wrapContent)

        }
    }
}