package app.suhocki.mybooks.ui.catalog.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import app.suhocki.mybooks.domain.model.Search
import app.suhocki.mybooks.ui.catalog.ui.SearchItemUI
import app.suhocki.mybooks.ui.catalog.listener.OnSearchClickListener
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk25.coroutines.textChangedListener

class SearchAdapterDelegate(
    private val search: Search,
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

        fun bind(search: Search) {
            with(ui) {
                editText.hint = editText.resources.getString(search.hintRes)
                editText.text = search.searchQuery
                editText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        onSearchClickListener.onStartSearchClick()
                        return@setOnEditorActionListener true
                    }
                    false
                }
                editText.textChangedListener {
                    onTextChanged { searchQuery, _, _, _ ->
                        this@SearchAdapterDelegate.search.searchQuery = searchQuery.toString()
                    }
                }
                startSearch.setOnClickListener { onSearchClickListener.onStartSearchClick() }
            }
        }
    }
}