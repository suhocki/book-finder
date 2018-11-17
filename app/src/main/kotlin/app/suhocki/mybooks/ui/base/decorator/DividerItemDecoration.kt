package app.suhocki.mybooks.ui.base.decorator

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.support.v7.widget.RecyclerView.State
import android.view.View
import app.suhocki.mybooks.domain.model.Header
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

class DividerItemDecoration(
    private val offsets: Int = 0,
    private val startFrom: Int = 0,
    private val divideOnlyHeaders: Boolean = false,
    private val divideTopAndBottom: Boolean = false
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        parent.layoutManager ?: throw RuntimeException("LayoutManager not found")
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        val itemPosition = layoutParams.viewAdapterPosition
        if (itemPosition < 0) return

        if (divideOnlyHeaders) {
            val items = (parent.adapter as ListDelegationAdapter<*>).items
            val current = items[itemPosition]
            if (current is Header) {
                outRect.top = offsets
                if (divideTopAndBottom && isNotUnderHeader(items, itemPosition)) {
                    outRect.bottom = offsets
                }
            }
        } else if (itemPosition > startFrom) {
            outRect.top = offsets
        }
    }

    private fun isNotUnderHeader(
        items: MutableList<*>,
        itemPosition: Int
    ): Boolean {
        val nextItemPosition = itemPosition + 1
        if (items.size > nextItemPosition) {
            if (items[nextItemPosition] is Header) {
                return false
            }
        }
        return true
    }
}