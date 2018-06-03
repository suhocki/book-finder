package app.suhocki.mybooks.ui.base.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.FilterCategory
import app.suhocki.mybooks.ui.base.adapter.ui.FilterCategoryItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.imageResource

class FilterCategoryAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        FilterCategoryItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is FilterCategory }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as FilterCategory)


    private inner class ViewHolder(val ui: FilterCategoryItemUI) :
        RecyclerView.ViewHolder(ui.parent) {
        fun bind(filterCategory: FilterCategory) {
            with(ui) {
                imageConfigurated.visibility =
                        if (filterCategory.isConfigurated) View.VISIBLE
                        else View.GONE
                imageState.imageResource =
                        if (filterCategory.isExpanded) R.drawable.ic_expand_less
                        else R.drawable.ic_expand_more
                title.text = filterCategory.title
            }
        }
    }
}