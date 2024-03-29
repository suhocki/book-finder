@file:Suppress("ClassName")

package app.suhocki.mybooks.ui.base

import android.content.Context
import android.support.design.button.MaterialButton
import android.support.design.card.MaterialCardView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.view.ViewManager
import app.suhocki.mybooks.ui.base.view.AutofitRecyclerView
import app.suhocki.mybooks.ui.base.view.MultilineCollapsingToolbarLayout
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.formats.NativeContentAdView
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

inline fun ViewManager.adView(init: (@AnkoViewDslMarker AdView).() -> Unit): AdView {
    return ankoView({ ctx: Context -> AdView(ctx) }, theme = 0) { init() }
}

inline fun ViewManager.nativeContentAdView(
    init: (@AnkoViewDslMarker NativeContentAdView).() -> Unit
) =
    ankoView({ ctx: Context -> NativeContentAdView(ctx) }, theme = 0) { init() }

inline fun ViewManager.multilineCollapsingToolbarLayout(
    init: (@AnkoViewDslMarker MultilineCollapsingToolbarLayout).() -> Unit
) = ankoView({ ctx: Context ->
    MultilineCollapsingToolbarLayout(
        ctx
    )
}, theme = 0) {
    init()
}

inline fun ViewManager.themedToolbarCompat(
    theme: Int = 0,
    init: (@AnkoViewDslMarker Toolbar).() -> Unit
) = ankoView({ ctx: Context -> Toolbar(ctx) }, theme) { init() }

inline fun ViewManager.ahBottomNavigation(
    theme: Int = 0,
    init: (@AnkoViewDslMarker AHBottomNavigation).() -> Unit
) = ankoView({ ctx: Context -> AHBottomNavigation(ctx) }, theme) { init() }

inline fun ViewManager.simpleDraweeView(
    theme: Int = 0,
    init: (@AnkoViewDslMarker SimpleDraweeView).() -> Unit
) = ankoView({ ctx: Context -> SimpleDraweeView(ctx) }, theme) { init() }

inline fun ViewManager.materialButton(
    theme: Int = 0,
    init: (@AnkoViewDslMarker MaterialButton).() -> Unit
) = ankoView({ ctx: Context -> MaterialButton(ctx) }, theme) { init() }

inline fun ViewManager.materialCardView(
    theme: Int = 0,
    init: (@AnkoViewDslMarker MaterialCardView).() -> Unit
) = ankoView({ ctx: Context -> MaterialCardView(ctx) }, theme) { init() }


