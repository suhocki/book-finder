package app.suhocki.mybooks.ui.base.decorator

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import app.suhocki.mybooks.R


class ItemDecoratorGrid : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val offset = view.resources.getDimensionPixelSize(R.dimen.height_divider_decorator)
        with(outRect) {
            right = offset
            top = offset
            left = offset
            bottom = offset
        }
    }
}
