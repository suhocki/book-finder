package app.suhocki.mybooks.ui.catalog.delegate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.domain.model.Banner
import app.suhocki.mybooks.setForegroundCompat
import app.suhocki.mybooks.ui.base.simpleDraweeView
import com.facebook.drawee.view.SimpleDraweeView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*

class BannerAdapterDelegate :
    AbsListItemAdapterDelegate<Banner, Any, BannerAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(Ui(parent.context))

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is Banner

    override fun onBindViewHolder(
        item: Banner,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)

    inner class ViewHolder(
        val ui: Ui
    ) : RecyclerView.ViewHolder(ui.parent) {
        fun bind(banner: Banner) {
            with(ui) {
                image.setImageURI(banner.imageUrl)
                description.text = banner.description
            }
        }
    }

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parent: View
        lateinit var image: SimpleDraweeView
        lateinit var description: TextView

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>) = with(ui) {

            frameLayout {
                this@Ui.parent = this

                frameLayout {
                    setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))

                    simpleDraweeView {
                        this@Ui.image = this
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }.lparams(matchParent, matchParent)

                    textView {
                        this@Ui.description = this
                        textAppearance = R.style.TextAppearance_AppCompat_Subhead
                        textColorResource = R.color.colorBlack
                        backgroundColorResource = R.color.colorDescriptionBackground
                        gravity = Gravity.CENTER_HORIZONTAL
                    }.lparams(matchParent, wrapContent) {
                        gravity = Gravity.BOTTOM
                        bottomMargin = dip(8)
                    }
                }
                lparams(matchParent, dimen(R.dimen.height_item_banner))
            }
        }
    }
}