package app.suhocki.mybooks.presentation.base

import android.content.Context
import android.view.View

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