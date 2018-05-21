package app.suhocki.mybooks.presentation.books

import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View


class ItemDecoratorGrid(private val offset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State?
    ) {
        val layoutManager = parent.layoutManager as GridLayoutManager
        val spanCount = layoutManager.spanCount
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        val itemPosition = layoutParams.viewAdapterPosition
        with(outRect) {
            right = if ((itemPosition + 1) % spanCount == 0) offset else 0
            top = offset
            left = offset
            var nearBottomViewCount = layoutManager.itemCount % spanCount
            if (nearBottomViewCount == 0) nearBottomViewCount = spanCount
            val lastPositions = layoutManager.itemCount - nearBottomViewCount
            bottom = if (itemPosition >= lastPositions) offset else 0
        }
    }
}
