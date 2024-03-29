package app.suhocki.mybooks.ui.base

import android.support.v7.util.ListUpdateCallback
import android.support.v7.widget.RecyclerView

class EndActionAdapterListUpdateCallback(
    private val mAdapter: RecyclerView.Adapter<*>,
    var endAction: (() -> Unit)? = null
) : ListUpdateCallback {

    /** {@inheritDoc}  */
    override fun onInserted(position: Int, count: Int) {
        mAdapter.notifyItemRangeInserted(position, count)
        endAction?.invoke()
        endAction = null
    }

    /** {@inheritDoc}  */
    override fun onRemoved(position: Int, count: Int) {
        mAdapter.notifyItemRangeRemoved(position, count)
        endAction?.invoke()
        endAction = null
    }

    /** {@inheritDoc}  */
    override fun onMoved(fromPosition: Int, toPosition: Int) {
        mAdapter.notifyItemMoved(fromPosition, toPosition)
        endAction?.invoke()
        endAction = null
    }

    /** {@inheritDoc}  */
    override fun onChanged(position: Int, count: Int, payload: Any?) {
        mAdapter.notifyItemRangeChanged(position, count, payload)
        endAction?.invoke()
        endAction = null
    }

    fun onLastEvent() {
        endAction?.invoke()
        endAction = null
    }
}
