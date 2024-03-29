package app.suhocki.mybooks.ui.filter.delegate

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.filter.FilterCategory
import app.suhocki.mybooks.ui.filter.listener.OnFilterCategoryClickListener
import app.suhocki.mybooks.ui.filter.ui.FilterCategoryItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.imageResource

class FilterCategoryAdapterDelegate(
    private val onFilterCategoryClickListener: OnFilterCategoryClickListener
) : AdapterDelegate<MutableList<Any>>() {

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


    private inner class ViewHolder(
        val ui: FilterCategoryItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var filterCategory: FilterCategory

        init {
            itemView.setOnClickListener {
                onFilterCategoryClickListener.onFilterCategoryClick(filterCategory)
            }
        }

        fun bind(filterCategory: FilterCategory) {
            this.filterCategory = filterCategory
            with(ui) {
                imageConfigurated.visibility =
                        if (filterCategory.checkedCount > 0) View.VISIBLE
                        else View.GONE
                imageState.imageResource =
                        if (filterCategory.isExpanded) R.drawable.ic_expand_less
                        else R.drawable.ic_expand_more
                title.text = filterCategory.title
            }
        }
    }
}