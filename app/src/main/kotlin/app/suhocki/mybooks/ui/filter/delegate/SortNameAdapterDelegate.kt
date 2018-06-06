package app.suhocki.mybooks.ui.filter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.filter.SortName
import app.suhocki.mybooks.ui.base.ui.FilterSubCategoryItemUI
import app.suhocki.mybooks.ui.filter.listener.OnSortNameToggleListener
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class SortNameAdapterDelegate(
    private val sortNameToggleListener: OnSortNameToggleListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        FilterSubCategoryItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is SortName }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as SortName)


    private inner class ViewHolder(
        val ui: FilterSubCategoryItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var filterName: SortName

        init {
            itemView.setOnClickListener { invert() }
            ui.checkBox.setOnClickListener{ invert(false) }
        }

        private fun invert(invertCheckBox: Boolean = true) {
            filterName.isChecked = !filterName.isChecked
            if (filterName.isChecked && filterName.groupItem!!.isChecked){
                filterName.groupItem!!.isChecked = false
                sortNameToggleListener.onSortNameToggle(filterName.groupItem!!)
            }
            if (invertCheckBox) ui.checkBox.isChecked = !ui.checkBox.isChecked
        }

        fun bind(filterName: SortName) {
            this.filterName = filterName
            with(ui) {
                checkBox.isChecked = filterName.isChecked
                name.text = filterName.sortName
            }
        }
    }
}