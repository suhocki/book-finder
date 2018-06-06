package app.suhocki.mybooks.ui.filter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.filter.SortPrice
import app.suhocki.mybooks.ui.base.ui.FilterSubCategoryItemUI
import app.suhocki.mybooks.ui.filter.listener.OnSortPriceToggleListener
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class SortPriceAdapterDelegate(
    private val onSortPriceToggleListener: OnSortPriceToggleListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        FilterSubCategoryItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is SortPrice }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as SortPrice)


    private inner class ViewHolder(
        val ui: FilterSubCategoryItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var sortPrice: SortPrice

        init {
            itemView.setOnClickListener { invert() }
            ui.checkBox.setOnClickListener{ invert(false) }
        }

        private fun invert(invertCheckBox: Boolean = true) {
            sortPrice.isChecked = !sortPrice.isChecked
            if (sortPrice.isChecked && sortPrice.groupItem!!.isChecked){
                sortPrice.groupItem!!.isChecked = false
                onSortPriceToggleListener.onSortPriceToggle(sortPrice.groupItem!!)
            }
            if (invertCheckBox) ui.checkBox.isChecked = !ui.checkBox.isChecked
        }

        fun bind(sortPrice: SortPrice) {
            this.sortPrice = sortPrice
            with(ui) {
                checkBox.isChecked = sortPrice.isChecked
                name.text = sortPrice.sortName
            }
        }
    }
}