package app.suhocki.mybooks.ui.catalog.delegate

import android.content.Context
import android.graphics.Color
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.delegate.ProgressAdapterDelegate
import app.suhocki.mybooks.ui.catalog.entity.BannersHolder
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AsyncListDifferDelegationAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class BannersHolderAdapterDelegate(
    nextPageListener: () -> Unit
) : AbsListItemAdapterDelegate<BannersHolder, Any, BannersHolderAdapterDelegate.ViewHolder>() {

    private var adapter = BannersAdapter(
        BannersAdapter.BannersDiffCallback(),
        nextPageListener
    )

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(Ui(parent.context))

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is BannersHolder

    override fun onBindViewHolder(
        item: BannersHolder,
        holder: BannersHolderAdapterDelegate.ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)

    inner class ViewHolder(
        val ui: BannersHolderAdapterDelegate.Ui
    ) : RecyclerView.ViewHolder(ui.parent) {
        init {
            ui.recyclerView.adapter = adapter
        }

        fun bind(bannersHolder: BannersHolder) {
            adapter.setData(bannersHolder.banners)
        }
    }

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parent: View
        lateinit var recyclerView: RecyclerView

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>) =
            ui.frameLayout {
                this@Ui.parent = this
                backgroundColor = Color.WHITE

                recyclerView {
                    recyclerView = this
                    id = Ids.recyclerBanners
                    clipToPadding = false
                    layoutManager = LinearLayoutManager(
                        context, LinearLayoutManager.HORIZONTAL, false
                    )

                    lparams(matchParent, wrapContent)
                }

                lparams(matchParent, wrapContent)
            }
    }

    private class BannersAdapter(
        diffCallback: BannersAdapter.BannersDiffCallback,
        private val nextPageListener: () -> Unit
    ) : AsyncListDifferDelegationAdapter<Any>(diffCallback) {

        init {
            delegatesManager
                .addDelegate(BannerAdapterDelegate())
                .addDelegate(ProgressAdapterDelegate())
        }

        fun setData(list: List<Any>) {
            items = list.toList()
        }

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
            payloads: MutableList<Any?>
        ) {
            super.onBindViewHolder(holder, position, payloads)

            if (position == items.lastIndex) nextPageListener()
        }

        class BannersDiffCallback : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any) = when {
                oldItem is Banner && newItem is Banner -> oldItem.id == newItem.id
                else -> oldItem::class.java == newItem::class.java
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any) = when {
                oldItem is Banner && newItem is Banner ->
                    oldItem.imageUrl == newItem.imageUrl &&
                            oldItem.description == newItem.description
                else -> true
            }
        }
    }
}