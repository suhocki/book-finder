package app.suhocki.mybooks.ui.filter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.filter.FilterPublisher
import app.suhocki.mybooks.ui.filter.listener.OnFilterPublisherClickListener
import app.suhocki.mybooks.ui.filter.ui.FilterCheckBoxItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class FilterPublisherAdapterDelegate(
    private val onFilterPublisherClickListener: OnFilterPublisherClickListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        FilterCheckBoxItemUI()
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
        val ui: FilterCheckBoxItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {

        private lateinit var filterPublisher: FilterPublisher

        init {
            itemView.setOnClickListener {
                onFilterPublisherClickListener.onFilterPublisherClick(filterPublisher)
            }
        }

        fun bind(filterPublisher: FilterPublisher) {
            this.filterPublisher = filterPublisher
            with(ui) {
                checkBox.isChecked = filterPublisher.isChecked
                name.text = filterPublisher.publisherName
                booksCount.text = filterPublisher.booksCount.toString()
            }
        }
    }
}