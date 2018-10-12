package app.suhocki.mybooks.ui.admin.decorator

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.support.v7.widget.RecyclerView.State
import android.view.View
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.domain.model.admin.UploadControl
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

class FileDecoration(private val offsets: Int) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        val layoutParams = view.layoutParams as RecyclerView.LayoutParams
        val itemPosition = layoutParams.viewAdapterPosition

        if (itemPosition < 0) return

        val items = (parent.adapter as ListDelegationAdapter<*>).items
        val current = items[itemPosition]
        val next =
            if (items.lastIndex > itemPosition) items[itemPosition.inc()]
            else null
        val isNextUploadControl = next != null && next is UploadControl
        if (current !is Header && !isNextUploadControl) {
            outRect.bottom = offsets
        }
    }
}