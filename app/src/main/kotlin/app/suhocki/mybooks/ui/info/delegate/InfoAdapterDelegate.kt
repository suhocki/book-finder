package app.suhocki.mybooks.ui.info.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.Info
import app.suhocki.mybooks.ui.info.listener.OnInfoClickListener
import app.suhocki.mybooks.ui.info.ui.InfoItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class InfoAdapterDelegate(
    private val onInfoClickListener: OnInfoClickListener
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        InfoItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Info }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Info)


    private inner class ViewHolder(val ui: InfoItemUI) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var info: Info

        init {
            itemView.setOnClickListener { onInfoClickListener.onInfoClick(info) }
        }

        fun bind(info: Info) {
            this.info = info
            with(ui) {
                name.text = info.name
                icon.setImageResource(info.iconRes)
            }
        }
    }
}