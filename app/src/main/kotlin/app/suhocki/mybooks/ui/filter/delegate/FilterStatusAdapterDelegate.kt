package app.suhocki.mybooks.ui.filter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.filter.FilterStatus
import app.suhocki.mybooks.ui.base.ui.FilterSubCategoryItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class FilterStatusAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        FilterSubCategoryItemUI()
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
        val ui: FilterSubCategoryItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {

        private lateinit var filterStatus: FilterStatus

        init {
            itemView.setOnClickListener { invert() }
            ui.checkBox.setOnClickListener{ invert(false) }
        }

        private fun invert(invertCheckBox: Boolean = true) {
            filterStatus.isChecked = !filterStatus.isChecked
            if (invertCheckBox) ui.checkBox.isChecked = !ui.checkBox.isChecked
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