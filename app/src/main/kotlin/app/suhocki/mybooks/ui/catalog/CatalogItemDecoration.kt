package app.suhocki.mybooks.ui.catalog

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.support.v7.widget.RecyclerView.State
import android.view.View
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.Category
import com.hannesdorfmann.adapterdelegates3.AsyncListDifferDelegationAdapter

class CatalogItemDecoration : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        val itemPosition = layoutParams.viewAdapterPosition

        if (itemPosition < 0) return

        val items = (parent.adapter as AsyncListDifferDelegationAdapter<*>).items
        val current = items[itemPosition]
        val next = items.getOrNull(itemPosition + 1)

        if (current is Category && next is Category) {
            outRect.bottom = view.resources.getDimensionPixelSize(R.dimen.height_divider_decorator)
        }
    }
}