package app.suhocki.mybooks.ui.base.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.ui.base.listener.OnSearchClickListener
import app.suhocki.mybooks.ui.base.ui.SearchItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.textChangedListener

class SearchAdapterDelegate(
    private val onSearchClickListener: OnSearchClickListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        SearchItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Search }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Search)


    private inner class ViewHolder(val ui: SearchItemUI) : RecyclerView.ViewHolder(ui.parent) {

        private lateinit var search: Search

        init {
            with(ui) {
                editText.textChangedListener {
                    onTextChanged { searchQuery, _, _, _ ->
                        search.searchQuery = searchQuery.toString()
                    }
                }
                editText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        onSearchClickListener.onStartSearchClick(search)
                        return@setOnEditorActionListener true
                    }
                    false
                }
                startSearch.setOnClickListener { onSearchClickListener.onStartSearchClick(search) }
            }
        }

        fun bind(search: Search) {
            this.search = search
            with(ui) {
                editText.hint = editText.resources.getString(search.hintRes)
                editText.text = search.searchQuery
            }
        }
    }
}