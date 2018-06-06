package app.suhocki.mybooks.ui.base.delegate

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.domain.model.filter.FilterAuthor
import app.suhocki.mybooks.setForegroundCompat
import app.suhocki.mybooks.ui.base.listener.OnFilterAuthorClickListener
import app.suhocki.mybooks.ui.base.ui.FilterSubCategoryItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class FilterAuthorAdapterDelegate(
    private val onFilterAuthorClickListener: OnFilterAuthorClickListener? = null
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        FilterSubCategoryItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is FilterAuthor }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as FilterAuthor)


    private inner class ViewHolder(
        val ui: FilterSubCategoryItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var filterAuthor: FilterAuthor

        init {
            itemView.setOnClickListener {
                invert()
                onFilterAuthorClickListener?.onFilterAuthorClick(filterAuthor)
            }
            ui.checkBox.setOnClickListener { invert(false) }
        }

        private fun invert(invertCheckBox: Boolean = true) {
            filterAuthor.isChecked = !filterAuthor.isChecked
            if (invertCheckBox) ui.checkBox.isChecked = !ui.checkBox.isChecked
        }

        fun bind(filterAuthor: FilterAuthor) {
            this.filterAuthor = filterAuthor
            with(ui) {
                if (!filterAuthor.isCheckable) {
                    checkBox.visibility = View.GONE
                    parent.setForegroundCompat(
                        parent.context.attrResource(R.attr.selectableItemBackground)
                    )
                }
                checkBox.isChecked = filterAuthor.isChecked
                name.text = filterAuthor.authorName
                booksCount.text = filterAuthor.booksCount.toString()
            }
        }
    }
}