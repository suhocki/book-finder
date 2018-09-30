package app.suhocki.mybooks.ui.base.decorator

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class ItemDecoratorGrid(private val offset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            right = offset
            top = offset
            left = offset
            bottom = offset
        }
    }
}
