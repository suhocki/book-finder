package app.suhocki.mybooks.ui.base.adapter.delegate.filter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.filter.FilterStatus
import app.suhocki.mybooks.ui.base.adapter.ui.filter.FilterCheckableItemUI
import app.suhocki.mybooks.ui.base.listener.filter.OnFilterStatusClickListener
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class FilterStatusAdapterDelegate(
    private val onFilterStatusClickListener: OnFilterStatusClickListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        FilterCheckableItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is FilterStatus }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as FilterStatus)


    private inner class ViewHolder(
        val ui: FilterCheckableItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {

        private lateinit var filterStatus: FilterStatus

        init {
            itemView.setOnClickListener {
                onFilterStatusClickListener.onFilterStatusClickClick(filterStatus)
            }
        }

        fun bind(filterStatus: FilterStatus) {
            this.filterStatus = filterStatus
            with(ui) {
                checkBox.isChecked = filterStatus.isChecked
                name.text = filterStatus.status
                booksCount.text = filterStatus.booksCount.toString()
            }
        }
    }
}