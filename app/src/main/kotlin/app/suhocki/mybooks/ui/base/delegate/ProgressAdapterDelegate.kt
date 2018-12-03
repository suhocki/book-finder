package app.suhocki.mybooks.ui.base.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.ui.admin.ui.ProgressItemUI
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.base.entity.PageProgress
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext


class ProgressAdapterDelegate : AdapterDelegate<MutableList<UiItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(ProgressItemUI().apply {
            createView(AnkoContext.createReusable(parent.context, parent, false))
        })

    override fun isForViewType(items: MutableList<UiItem>, position: Int): Boolean =
        with(items[position]) { this is PageProgress }

    override fun onBindViewHolder(
        items: MutableList<UiItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = Unit

    private inner class ViewHolder(ui: ProgressItemUI) : RecyclerView.ViewHolder(ui.parent)
}