package app.suhocki.mybooks.ui.admin.delegate

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.domain.model.admin.UploadControl
import app.suhocki.mybooks.ui.admin.ui.UploadControlItemUI
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.childrenRecursiveSequence
import org.jetbrains.anko.childrenSequence
import org.jetbrains.anko.sdk25.coroutines.onClick


class UploadControlAdapterDelegate(
    private val cancelUpload: () -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val ui = UploadControlItemUI().apply {
            createView(AnkoContext.createReusable(parent.context, parent, false))
        }
        ui.cancel.onClick { cancelUpload() }
        return ViewHolder(ui)
    }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean =
        with(items[position]) { this is UploadControl }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position] as UploadControl)


    private inner class ViewHolder(
        val ui: UploadControlItemUI
    ) : RecyclerView.ViewHolder(ui.parent) {

        fun bind(uploadControl: UploadControl) {
            var isStepItemFound = false
            val stepHumanName = ui.parent.resources.getString(uploadControl.stepRes)

            ui.parent.childrenSequence().filterNot { it is TextView }.forEach { view ->
                val progressBar = view.childrenRecursiveSequence()
                    .find { it is ProgressBar }!!

                val imageSuccess = view.childrenRecursiveSequence()
                    .find { it is ImageView }!!

                val stepText = view.childrenRecursiveSequence()
                    .find { it is TextView } as TextView

                val progressText = view.childrenRecursiveSequence()
                    .findLast { it is TextView } as TextView

                if (stepText.text == stepHumanName) {
                    isStepItemFound = true
                    imageSuccess.visibility = View.INVISIBLE
                    if (uploadControl.progress > 0) {
                        progressBar.visibility = View.INVISIBLE
                        progressText.visibility = View.VISIBLE
                        progressText.text = progressText.resources
                            .getString(R.string.percent, uploadControl.progress)
                    } else {
                        progressBar.visibility = View.VISIBLE
                    }
                } else {
                    progressBar.visibility = View.INVISIBLE
                    progressText.visibility = View.INVISIBLE
                    imageSuccess.visibility =
                            if (isStepItemFound) View.INVISIBLE
                            else View.VISIBLE
                }
            }
        }
    }
}