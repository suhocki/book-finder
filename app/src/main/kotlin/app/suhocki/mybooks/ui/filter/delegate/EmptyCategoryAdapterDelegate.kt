package app.suhocki.mybooks.ui.filter.delegate

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.filter.EmptyCategory
import app.suhocki.mybooks.domain.model.filter.FilterCategory
import app.suhocki.mybooks.ui.filter.ui.EmptyCategoryItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class EmptyCategoryAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        EmptyCategoryItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is EmptyCategory }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as EmptyCategory)


    private inner class ViewHolder(
        val ui: EmptyCategoryItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {

        fun bind(emptyCategory: EmptyCategory) {
            with(ui) {
                name.text = emptyCategory.name
            }
        }
    }
}