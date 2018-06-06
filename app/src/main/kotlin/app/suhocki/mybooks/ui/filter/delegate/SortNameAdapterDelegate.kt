package app.suhocki.mybooks.ui.filter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.filter.SortName
import app.suhocki.mybooks.ui.base.ui.FilterSubCategoryItemUI
import app.suhocki.mybooks.ui.filter.listener.SortNameListener
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class SortNameAdapterDelegate(
    private val sortNameListener: SortNameListener
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
        private lateinit var sortName: SortName

        init {
            itemView.setOnClickListener { invert() }
            ui.checkBox.setOnClickListener{ invert(false) }
        }

        private fun invert(checkBoxUnchanged: Boolean = true) {
            sortName.isChecked = !sortName.isChecked
            if (sortName.isChecked && sortName.groupItem!!.isChecked){
                sortName.groupItem!!.isChecked = false
                sortNameListener.onSortNameToggle(sortName.groupItem!!)
            }
            if (checkBoxUnchanged) ui.checkBox.isChecked = !ui.checkBox.isChecked
            sortNameListener.onSortNameClick(sortName)
        }

        fun bind(filterName: SortName) {
            this.sortName = filterName
            with(ui) {
                checkBox.isChecked = filterName.isChecked
                name.text = filterName.sortName
            }
        }
    }
}