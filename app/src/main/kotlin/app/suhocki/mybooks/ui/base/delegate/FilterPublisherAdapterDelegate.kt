package app.suhocki.mybooks.ui.base.delegate

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.domain.model.filter.FilterPublisher
import app.suhocki.mybooks.setForegroundCompat
import app.suhocki.mybooks.ui.base.listener.OnFilterPublisherClickListener
import app.suhocki.mybooks.ui.base.ui.FilterSubCategoryItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class FilterPublisherAdapterDelegate(
    private val onFilterPublisherClickListener: OnFilterPublisherClickListener? = null
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        FilterSubCategoryItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is FilterPublisher }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as FilterPublisher)


    private inner class ViewHolder(
        val ui: FilterSubCategoryItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {

        private lateinit var filterPublisher: FilterPublisher

        init {
            itemView.setOnClickListener {
                invert()
                onFilterPublisherClickListener?.onFilterPublisherClick(filterPublisher)
            }
            ui.checkBox.setOnClickListener { invert(false) }
        }

        private fun invert(invertCheckBox: Boolean = true) {
            filterPublisher.isChecked = !filterPublisher.isChecked
            if (invertCheckBox) ui.checkBox.isChecked = !ui.checkBox.isChecked
        }

        fun bind(filterPublisher: FilterPublisher) {
            this.filterPublisher = filterPublisher
            with(ui) {

                if (!filterPublisher.isCheckable) {
                    checkBox.visibility = View.GONE
                    parent.setForegroundCompat(
                        parent.context.attrResource(R.attr.selectableItemBackground)
                    )
                }
                checkBox.isChecked = filterPublisher.isChecked
                name.text = filterPublisher.publisherName
                booksCount.text = filterPublisher.booksCount.toString()
            }
        }
    }
}