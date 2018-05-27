@file:Suppress("ClassName")

package app.suhocki.mybooks.presentation.base

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewManager
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import org.jetbrains.anko.AnkoViewDslMarker
import org.jetbrains.anko.custom.ankoView

open class MultilineCollapsingToolbarLayout(ctx: Context): net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout(ctx) {
    inline fun <T: View> T.lparams(
        width: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        height: Int = android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        init: net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout.LayoutParams.() -> Unit
    ): T {
        val layoutParams = net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout.LayoutParams(width, height)
        layoutParams.init()
        this@lparams.layoutParams = layoutParams
        return this
    }
}

open class _AutofitRecyclerView(ctx: Context): AutofitRecyclerView(ctx) {
    inline fun <T: View> T.lparams(
        width: Int = android.view.ViewGroup.LayoutParams.MATCH_PARENT,
        height: Int = android.view.ViewGroup.LayoutParams.MATCH_PARENT,
        init: AutofitRecyclerView.LayoutParams.() -> Unit
    ): T {
        val layoutParams = AutofitRecyclerView.LayoutParams(width, height)
        layoutParams.init()
        this@lparams.layoutParams = layoutParams
        return this
    }
}

inline fun ViewManager.textViewCompat(init: (@AnkoViewDslMarker AppCompatTextView).() -> Unit): AppCompatTextView {
    return ankoView({ ctx: Context -> AppCompatTextView(ctx) }, theme = 0) { init() }
}

inline fun ViewManager.themedToolbarCompat(theme: Int = 0, init: (@AnkoViewDslMarker Toolbar).() -> Unit): Toolbar {
    return ankoView({ ctx: Context -> Toolbar(ctx) }, theme) { init() }
}

inline fun ViewManager.bottomNavigation(theme: Int = 0, init: (@AnkoViewDslMarker AHBottomNavigation).() -> Unit): AHBottomNavigation {
    return ankoView({ ctx: Context -> AHBottomNavigation(ctx) }, theme) { init() }
}
