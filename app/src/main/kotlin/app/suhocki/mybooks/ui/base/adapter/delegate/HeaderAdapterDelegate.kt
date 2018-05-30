package app.suhocki.mybooks.ui.base.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.ui.base.adapter.ui.HeaderItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class HeaderAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        HeaderItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Header }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Header)


    private inner class ViewHolder(val ui: HeaderItemUI) : RecyclerView.ViewHolder(ui.parent) {
        fun bind(header: Header) {
            with(ui) {
                title.text = parent.resources.getString(header.titleRes)
            }
        }
    }
}