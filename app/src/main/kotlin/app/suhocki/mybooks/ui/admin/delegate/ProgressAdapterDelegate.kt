package app.suhocki.mybooks.ui.admin.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.ui.admin.entity.Progress
import app.suhocki.mybooks.ui.admin.ui.ProgressItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext


class ProgressAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(ProgressItemUI().apply {
            createView(AnkoContext.createReusable(parent.context, parent, false))
        })

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Progress }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = Unit

    private inner class ViewHolder(ui: ProgressItemUI) : RecyclerView.ViewHolder(ui.parent)
}