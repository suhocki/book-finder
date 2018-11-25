package app.suhocki.mybooks.ui.catalog.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.data.ads.AdsManager
import app.suhocki.mybooks.domain.model.BannerAd
import app.suhocki.mybooks.ui.base.entity.UiItem
import app.suhocki.mybooks.ui.catalog.ui.BannerAdItemUI
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoLogger

class BannerAdAdapterDelegate : AdapterDelegate<MutableList<UiItem>>(), AnkoLogger {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        BannerAdItemUI()
            .apply { createView(AnkoContext.createReusable(parent.context, parent, false)) }
            .let { ViewHolder(it) }

    override fun isForViewType(items: MutableList<UiItem>, position: Int): Boolean =
        with(items[position]) { this is BannerAd }

    override fun onBindViewHolder(
        items: MutableList<UiItem>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as BannerAd)


    private inner class ViewHolder(val ui: BannerAdItemUI) : RecyclerView.ViewHolder(ui.parent) {
        private val adRequest by lazy {
            AdRequest.Builder()
                .addTestDevice(AdsManager.TEST_DEVICE_ID)
                .build()
        }

        fun bind(bannerAd: BannerAd) {
            val currentAdId = ui.adView.adUnitId
            if (currentAdId.isNullOrBlank() || currentAdId != bannerAd.bannerId) {
                with(ui.adView) {
                    adUnitId = bannerAd.bannerId
                    adSize = AdSize.SMART_BANNER
                    loadAd(adRequest)
                }
            }
        }
    }
}