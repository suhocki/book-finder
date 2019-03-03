package app.suhocki.mybooks.ui.base.delegate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import app.suhocki.mybooks.R
import app.suhocki.mybooks.ui.base.entity.Progress
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import org.jetbrains.anko.*


class ProgressAdapterDelegate :
    AbsListItemAdapterDelegate<Progress, Any, ProgressAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(Ui(parent.context))

    override fun isForViewType(
        item: Any, items: MutableList<Any>,
        position: Int
    ) = items[position] is Progress

    override fun onBindViewHolder(
        item: Progress,
        holder: ProgressAdapterDelegate.ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.ui.parent.layoutParams =
                if (item.horizontal) {
                    ViewGroup.LayoutParams(wrapContent, matchParent)
                } else {
                    ViewGroup.LayoutParams(matchParent, wrapContent)
                }
    }

    inner class ViewHolder(val ui: ProgressAdapterDelegate.Ui) : RecyclerView.ViewHolder(ui.parent)

    inner class Ui(context: Context) : AnkoComponent<Context> {
        lateinit var parent: View

        init {
            createView(AnkoContext.create(context, context, false))
        }

        override fun createView(ui: AnkoContext<Context>) =
            ui.frameLayout {
                this@Ui.parent = this

                themedProgressBar(R.style.AccentProgressBar)
                    .lparams {
                        gravity = Gravity.CENTER
                    }

                lparams {
                    margin = dip(12)
                }
            }
    }

}