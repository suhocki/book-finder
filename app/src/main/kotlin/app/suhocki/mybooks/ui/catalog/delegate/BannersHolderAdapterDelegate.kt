package app.suhocki.mybooks.ui.catalog.delegate

import android.content.Context
import android.graphics.Color
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.delegate.ProgressAdapterDelegate
import app.suhocki.mybooks.ui.catalog.CatalogFragment
import app.suhocki.mybooks.ui.catalog.entity.BannersHolder
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AsyncListDifferDelegationAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onTouch

private const val SPEED_COEFFICIENT = 125.0f

class BannersHolderAdapterDelegate(
    nextPageListener: () -> Unit,
    private val bannerController: CatalogFragment.BannersController
) : AbsListItemAdapterDelegate<BannersHolder, Any, BannersHolderAdapterDelegate.ViewHolder>(),
    AnkoLogger {

    private val adapter = BannersAdapter(
        BannersDiffCallback(),
        nextPageListener
    )

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                bannerController.onVisibleIndexChanged(
                    layoutManager.findFirstVisibleItemPosition()
                )
            }
            super.onScrollStateChanged(recyclerView, newState)
        }
    }

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var scroller: LinearSmoothScroller

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
            scroller = object : LinearSmoothScroller(ui.recyclerView.context) {
                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                    return SPEED_COEFFICIENT / displayMetrics!!.densityDpi.toFloat()
                }
            }
            layoutManager = LinearLayoutManager(
                ui.recyclerView.context, LinearLayoutManager.HORIZONTAL, false
            )

            ui.recyclerView.adapter = adapter
            ui.recyclerView.layoutManager = layoutManager
            ui.recyclerView.addOnScrollListener(onScrollListener)
            ui.recyclerView.onTouch { _, event ->
                val action = event.action
                val isReleaseAction = action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_CANCEL
                val isTouchAction = action == MotionEvent.ACTION_DOWN ||
                        action == MotionEvent.ACTION_MOVE

                if (isReleaseAction) bannerController.enableAutoScroll(true)
                else if (isTouchAction) bannerController.enableAutoScroll(false)
            }
            PagerSnapHelper().attachToRecyclerView(ui.recyclerView)

            bannerController.indexToShowReceiver = { index: Int ->
                scroller.targetPosition = index
                layoutManager.startSmoothScroll(scroller)
            }
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

                    lparams(matchParent, wrapContent)
                }

                lparams(matchParent, wrapContent)
            }
    }

    private inner class BannersAdapter(
        diffCallback: BannersDiffCallback,
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