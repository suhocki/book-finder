package app.suhocki.mybooks.ui.catalog.delegate

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.Header
import app.suhocki.mybooks.ui.catalog.ui.HeaderItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textAppearance

class HeaderAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        HeaderItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is Header }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Header)


    private inner class ViewHolder(val ui: HeaderItemUI) : RecyclerView.ViewHolder(ui.parent) {
        fun bind(header: Header) {
            with(ui) {
                if (!header.inverseColors) {
                    parent.backgroundResource = R.color.colorPrimary
                    title.textAppearance = R.style.TextAppearance_AppCompat_Subhead_Inverse
                    title.setTypeface(title.typeface, Typeface.BOLD)
                }
                title.setAllCaps(header.allCaps)
                if (!header.allCaps) {
                    parent.backgroundResource = R.color.colorWhite
                    title.textAppearance = R.style.TextAppearance_AppCompat_Subhead
                    title.setTypeface(title.typeface, Typeface.NORMAL)
                }
                title.text = header.title
            }
        }
    }
}