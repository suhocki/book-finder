package app.suhocki.mybooks.ui.catalog.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.ui.base.adView
import com.google.android.gms.ads.AdView
import org.jetbrains.anko.*

class BannerAdItemUI : AnkoComponent<ViewGroup> {
    lateinit var parent: View
    lateinit var adView: AdView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        frameLayout {
            this@BannerAdItemUI.parent = this

            adView {
                adView = this
            }.lparams(matchParent, wrapContent)

        }.apply {
            rootView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}