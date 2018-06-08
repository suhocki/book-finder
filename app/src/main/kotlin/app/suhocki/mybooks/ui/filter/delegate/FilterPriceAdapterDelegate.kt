package app.suhocki.mybooks.ui.filter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.filter.FilterPrice
import app.suhocki.mybooks.ui.filter.listener.OnFilterPriceChangeListener
import app.suhocki.mybooks.ui.filter.ui.FilterPriceItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.textChangedListener

class FilterPriceAdapterDelegate(
    private val onFilterPriceChangeListener: OnFilterPriceChangeListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        FilterPriceItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is FilterPrice }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as FilterPrice)


    private inner class ViewHolder(
        val ui: FilterPriceItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var filterPrice: FilterPrice

        init {
            ui.from.textChangedListener {
                onTextChanged { charSequence, _, _, _ ->
                    val isBlank = charSequence.isNullOrBlank()
                    filterPrice.from =
                            if (isBlank) 0.0
                            else charSequence.toString().toDouble()
                    onFilterPriceChangeListener.onFilterPriceChange(FilterPrice.FilterPriceType.FROM)
                }
            }
            ui.to.textChangedListener {
                onTextChanged { charSequence, _, _, _ ->
                    val isBlank = charSequence.isNullOrBlank()
                    filterPrice.to =
                            if (isBlank) Integer.MAX_VALUE.toDouble()
                            else charSequence.toString().toDouble()
                    onFilterPriceChangeListener.onFilterPriceChange(FilterPrice.FilterPriceType.TO)
                }
            }
        }

        fun bind(filterPrice: FilterPrice) {
            this.filterPrice = filterPrice
            with(ui) {
                if (filterPrice.from == 0.0) from.setText("")
                if (filterPrice.to == Integer.MAX_VALUE.toDouble()) from.setText("")
                from.hint = parent.context.getString(R.string.rubles, filterPrice.hintFrom)
                    .dropLast(1)
                    .replace(",", ".")
                to.hint = parent.context.getString(R.string.rubles, filterPrice.hintTo)
                    .dropLast(1)
                    .replace(",", ".")
            }
        }
    }
}