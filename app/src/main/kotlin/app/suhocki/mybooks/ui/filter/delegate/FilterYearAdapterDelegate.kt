package app.suhocki.mybooks.ui.filter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.filter.FilterYear
import app.suhocki.mybooks.ui.base.ui.FilterSubCategoryItemUI
import app.suhocki.mybooks.ui.filter.listener.OnFilterYearClickListener
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class FilterYearAdapterDelegate(
    private val yearClickListener: OnFilterYearClickListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        FilterSubCategoryItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is FilterYear }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as FilterYear)


    private inner class ViewHolder(
        val ui: FilterSubCategoryItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {

        private lateinit var filterYear: FilterYear

        init {
            itemView.setOnClickListener { invert() }
            ui.checkBox.setOnClickListener{ invert(false) }
        }

        private fun invert(invertCheckBox: Boolean = true) {
            filterYear.isChecked = !filterYear.isChecked
            if (invertCheckBox) ui.checkBox.isChecked = !ui.checkBox.isChecked
            yearClickListener.onFilterYearClick(filterYear)
        }

        fun bind(filterYear: FilterYear) {
            this.filterYear = filterYear
            with(ui) {
                checkBox.isChecked = filterYear.isChecked
                name.text = filterYear.year
                booksCount.text = filterYear.booksCount.toString()
            }
        }
    }
}