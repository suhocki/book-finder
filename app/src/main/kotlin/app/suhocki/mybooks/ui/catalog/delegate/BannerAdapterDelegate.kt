package app.suhocki.mybooks.ui.catalog.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.catalog.ui.BannerItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext

class BannerAdapterDelegate : AdapterDelegate<MutableList<UiItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        BannerItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<UiItem>, position: Int): Boolean =
        with(items[position]) { this is Banner }

    override fun onBindViewHolder(
        items: MutableList<UiItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as Banner)


    private inner class ViewHolder(val ui: BannerItemUI) : RecyclerView.ViewHolder(ui.parent) {
        fun bind(banner: Banner) {
            with(ui) {
                image.setImageURI(banner.imageUrl)
                description.text = banner.description
            }
        }
    }
}