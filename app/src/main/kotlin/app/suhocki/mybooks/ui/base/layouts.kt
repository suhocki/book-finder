@file:Suppress("ClassName")

package app.suhocki.mybooks.ui.base

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.ViewManager
import app.suhocki.mybooks.ui.base.view.AutofitRecyclerView
import app.suhocki.mybooks.ui.base.view.MultilineCollapsingToolbarLayout
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import org.jetbrains.anko.AnkoViewDslMarker
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.internals.AnkoInternals

inline fun ViewManager.themedAutofitRecyclerView(
    theme: Int = 0,
    init: (@AnkoViewDslMarker AutofitRecyclerView).() -> Unit
): AutofitRecyclerView {
    return ankoView({ ctx: Context ->
        val themedCtx = AnkoInternals.wrapContextIfNeeded(ctx, theme)
        AutofitRecyclerView(themedCtx)
    }, theme = 0) { init() }
}

inline fun ViewManager.textViewCompat(init: (@AnkoViewDslMarker AppCompatTextView).() -> Unit): AppCompatTextView {
    return ankoView({ ctx: Context -> AppCompatTextView(ctx) }, theme = 0) { init() }
}

inline fun ViewManager.multilineCollapsingToolbarLayout(init: (@AnkoViewDslMarker MultilineCollapsingToolbarLayout).() -> Unit): MultilineCollapsingToolbarLayout {
    return ankoView({ ctx: Context ->
        MultilineCollapsingToolbarLayout(
            ctx
        )
    }, theme = 0) { init() }
}

inline fun ViewManager.themedToolbarCompat(
    theme: Int = 0,
    init: (@AnkoViewDslMarker Toolbar).() -> Unit
): Toolbar {
    return ankoView({ ctx: Context -> Toolbar(ctx) }, theme) { init() }
}

inline fun ViewManager.bottomNavigation(
    theme: Int = 0,
    init: (@AnkoViewDslMarker AHBottomNavigation).() -> Unit
): AHBottomNavigation {
    return ankoView({ ctx: Context -> AHBottomNavigation(ctx) }, theme) { init() }
}
