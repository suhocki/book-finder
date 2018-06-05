package app.suhocki.mybooks.ui.filter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.filter.FilterAuthor
import app.suhocki.mybooks.ui.filter.listener.OnFilterAuthorClickListener
import app.suhocki.mybooks.ui.filter.ui.FilterCheckBoxItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class FilterAuthorAdapterDelegate(
    private val onFilterAuthorClickListener: OnFilterAuthorClickListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        FilterCheckBoxItemUI()
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
        val ui: FilterCheckBoxItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var filterAuthor: FilterAuthor

        init {
            itemView.setOnClickListener {
                onFilterAuthorClickListener.onFilterAuthorClick(filterAuthor)
            }
        }

        fun bind(filterAuthor: FilterAuthor) {
            this.filterAuthor = filterAuthor
            with(ui) {
                checkBox.isChecked = filterAuthor.isChecked
                name.text = filterAuthor.authorName
                booksCount.text = filterAuthor.booksCount.toString()
            }
        }
    }
}