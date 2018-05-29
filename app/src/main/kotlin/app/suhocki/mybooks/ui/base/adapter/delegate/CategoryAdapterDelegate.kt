package app.suhocki.mybooks.ui.base.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.ui.base.adapter.listener.OnCategoryClickListener
import app.suhocki.mybooks.ui.base.adapter.ui.CategoryItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContextImpl

class CategoryAdapterDelegate(
    private val onCategoryClickListener: OnCategoryClickListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        CategoryItemUI()
            .apply { createView(AnkoContextImpl(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Category }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Category)


    private inner class ViewHolder(val ui: CategoryItemUI) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var category: Category

        init {
            itemView.setOnClickListener { onCategoryClickListener.onCategoryClick(category) }
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