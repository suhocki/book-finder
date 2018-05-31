package app.suhocki.mybooks.ui.base.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.Hint
import app.suhocki.mybooks.ui.base.adapter.ui.HintItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class HintAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        HintItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Hint }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Hint)


    private inner class ViewHolder(val ui: HintItemUI) : RecyclerView.ViewHolder(ui.parent) {
        fun bind(hint: Hint) {
            ui.textView.text = ui.parent.resources.getString(hint.hintRes)
        }
    }
}