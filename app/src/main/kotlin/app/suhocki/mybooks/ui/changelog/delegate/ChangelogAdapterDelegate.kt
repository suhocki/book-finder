package app.suhocki.mybooks.ui.changelog.delegate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import app.suhocki.mybooks.domain.model.Changelog
import app.suhocki.mybooks.setForegroundCompat
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*
import java.text.SimpleDateFormat
import java.util.*


class ChangelogAdapterDelegate(
    private val onDownloadFileClick: (String) -> Unit
) : AbsListItemAdapterDelegate<Changelog, Any, ChangelogAdapterDelegate.ViewHolder>() {

    private val formatter by lazy { SimpleDateFormat(DATE_FORMAT, Locale.getDefault()) }

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(Ui(parent.context))

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is Changelog

    override fun onBindViewHolder(
        item: Changelog,
        holder: ChangelogAdapterDelegate.ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)

    inner class ViewHolder(
        val ui: Ui
    ) : RecyclerView.ViewHolder(ui.parent) {
        private lateinit var changelog: Changelog

        init {
            ui.download.setOnClickListener {
                val url = changelog.link
                url?.let { onDownloadFileClick(url) }
            }
        }

        fun bind(changelog: Changelog) {
            this.changelog = changelog
            with(ui) {
                version.text = formatter.format(Date(changelog.date * 1000L))
                date.text =
                        ui.parent.resources.getString(R.string.version_changelog, changelog.version)
                setChanges(changelog.changes)
                download.visibility =
                        if (changelog.link == null) View.INVISIBLE
                        else View.VISIBLE
            }
        }
    }

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parent: ViewGroup
        lateinit var date: TextView
        lateinit var version: TextView
        lateinit var download: View

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>) = with(ui) {

            verticalLayout {
                this@Ui.parent = this
                setForegroundCompat(context.attrResource(R.attr.selectableItemBackground))
                backgroundColorResource = R.color.colorWhite
                padding = dip(16)

                linearLayout {

                    verticalLayout {
                        textView {
                            date = this
                            ellipsize = TextUtils.TruncateAt.END
                            maxLines = 1
                            textAppearance = R.style.TextAppearance_AppCompat_Body2
                        }.lparams(wrapContent, wrapContent) {
                            weight = 1f
                        }

                        textView {
                            version = this
                            ellipsize = TextUtils.TruncateAt.END
                            maxLines = 1
                            textAppearance = R.style.TextAppearance_AppCompat_Body1
                        }.lparams(wrapContent, wrapContent) {
                            weight = 1f
                            topMargin = dip(4)
                        }
                    }.lparams(0, ViewGroup.LayoutParams.WRAP_CONTENT) {
                        weight = 1F
                    }

                    imageView(R.drawable.ic_download_file) {
                        download = this

                        padding = dimen(R.dimen.padding_toolbar_icon)
                        backgroundResource = context
                            .attrResource(R.attr.selectableItemBackgroundBorderless)
                    }.lparams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
                lparams(matchParent, wrapContent)
            }

        }

        fun setChanges(changes: Array<String>) {
            val oldViews = mutableListOf<View>()
            parent.childrenSequence().forEach {
                if (it.tag == TEMPORARY_TEXT_VIEW) oldViews.add(it)
            }
            oldViews.forEach { parent.removeView(it) }

            val ankoContext = AnkoContext.createReusable(parent.context, parent, false)

            changes.forEach {
                val textView = ankoContext.textView(it)
                textView.tag = TEMPORARY_TEXT_VIEW
                parent.addView(textView)
            }
        }
    }

    companion object {
        private const val DATE_FORMAT = "dd MMM yyyy"
        private const val TEMPORARY_TEXT_VIEW = "TEMPORARY_TEXT_VIEW"
    }
}