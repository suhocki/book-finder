package app.suhocki.mybooks.ui.base.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.ui.base.adapter.ui.SearchItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContextImpl

class SearchAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        SearchItemUI()
            .apply { createView(AnkoContextImpl(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Search }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Search)


    private inner class ViewHolder(val ui: SearchItemUI) : RecyclerView.ViewHolder(ui.parent) {
        fun bind(search: Search) {
            with(ui) {
                hint.text = parent.resources.getString(search.hintRes)
            }
        }
    }
}