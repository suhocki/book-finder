package app.suhocki.mybooks.ui.licenses.delegate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.domain.model.License
import app.suhocki.mybooks.getHumanName
import app.suhocki.mybooks.setForegroundCompat
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*


class LicenseAdapterDelegate(
    private val onLicenseClick: (License) -> Unit
) : AbsListItemAdapterDelegate<License, Any, LicenseAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(Ui(parent.context))

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is License

    override fun onBindViewHolder(
        item: License,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)

    inner class ViewHolder(
        val ui: Ui
    ) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var license: License

        init {
            itemView.setOnClickListener { onLicenseClick(license) }
        }

        fun bind(license: License) {
            this.license = license
            ui.name.text = license.name
            ui.license.text = license.license.getHumanName(ui.parent.resources)
        }
    }

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parent: ViewGroup
        lateinit var name: TextView
        lateinit var license: TextView

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>) = with(ui) {

            verticalLayout {
                this@Ui.parent = this
                setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))
                backgroundColorResource = R.color.colorWhite
                padding = dip(16)

                textView {
                    name = this
                    ellipsize = TextUtils.TruncateAt.END
                    maxLines = 1
                    textAppearance = R.style.TextAppearance_AppCompat_Body2
                }.lparams(wrapContent, wrapContent) {
                    weight = 1f
                }

                textView {
                    license = this
                    ellipsize = TextUtils.TruncateAt.END
                    maxLines = 1
                    textAppearance = R.style.TextAppearance_AppCompat_Body1
                }.lparams(wrapContent, wrapContent) {
                    weight = 1f
                    topMargin = dip(4)
                }

                lparams(matchParent, wrapContent)
            }
        }
    }

}