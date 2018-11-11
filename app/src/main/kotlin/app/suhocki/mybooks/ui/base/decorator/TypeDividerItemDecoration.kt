package app.suhocki.mybooks.ui.base.decorator

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.support.v7.widget.RecyclerView.State
import android.view.View
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

class TypeDividerItemDecoration(
    private val offsets: Int = 0,
    private val divideClass: Class<*>
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

        val items = (parent.adapter as ListDelegationAdapter<*>).items
        val current = items.getOrNull(itemPosition)
        val next = items.getOrNull(itemPosition.inc())

        if (next != null &&
            current != null &&
            (divideClass as Class).isAssignableFrom(current::class.java) &&
            (divideClass as Class).isAssignableFrom(next::class.java)
        ) {
            outRect.bottom = offsets
        }
    }
}