package app.suhocki.mybooks.ui.catalog.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.catalog.ui.CategoryItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class CategoryAdapterDelegate(
    private val onCategoryClick: (String) -> Unit
) : AdapterDelegate<MutableList<UiItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        CategoryItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<UiItem>, position: Int): Boolean =
        with(items[position]) { this is Category }

    override fun onBindViewHolder(
        items: MutableList<UiItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Category)


    private inner class ViewHolder(val ui: CategoryItemUI) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var category: Category

        init {
            itemView.setOnClickListener { onCategoryClick(category.id) }
        }

        fun bind(category: Category) {
            this.category = category
            with(ui) {
                name.text = category.name
                booksCount.text = category.booksCount.toString()
            }
        }
    }
}