package app.suhocki.mybooks.presentation.initial

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.suhocki.mybooks.Analytics
import app.suhocki.mybooks.R
import app.suhocki.mybooks.attrResource
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

class InitialUI @Inject constructor() : AnkoComponent<InitialActivity> {

    internal lateinit var textTitle: TextView
    internal lateinit var textDescription: TextView
    internal lateinit var ivTop: ImageView

    internal lateinit var textProgress: TextView
    internal lateinit var progressBar: View
    internal lateinit var btnExit: View
    internal lateinit var btnDownload: View
    internal lateinit var btnCancel: View
    internal lateinit var btnContinue: View
    internal lateinit var btnRetry: View
    internal lateinit var btnInBackground: View

    override fun createView(ui: AnkoContext<InitialActivity>) = with(ui) {

        verticalLayout {
            frameLayout {
                backgroundResource = R.color.colorPrimary

                imageView(R.drawable.logo) {
                    ivTop = this
                }.lparams(height = dip(48)) {
                    gravity = Gravity.CENTER
                    margin = dip(28)
                }
            }.lparams(matchParent, wrapContent)

            textView(R.string.info) {
                textTitle = this
                gravity = Gravity.CENTER
                textAppearance = R.style.TextAppearance_AppCompat_Title
            }.lparams {
                topMargin = dip(24)
                leftMargin = dip(24)
                rightMargin = dip(24)
            }

            linearLayout {

                themedProgressBar(R.style.ColoredProgressBar) {
                    progressBar = this
                    visibility = View.GONE
                }.lparams {
                    gravity = Gravity.CENTER
                    leftMargin = dip(22)
                }

                textView(resources.getString(R.string.database_load_need)) {
                    textDescription = this
                    textAppearance = R.style.TextAppearance_AppCompat_Body1
                }.lparams {
                    leftMargin = dip(24)
                    gravity = Gravity.CENTER_VERTICAL
                }

                textView {
                    textProgress = this
                    textAppearance = R.style.TextAppearance_AppCompat_Body1
                }.lparams {
                    gravity = Gravity.CENTER
                }
            }.lparams(matchParent, dip(64)) {
                topMargin = dip(16)
                rightMargin = dip(24)
            }

            frameLayout {
                backgroundResource = R.color.colorGray
            }.lparams(matchParent, dip(2)) {
                topMargin = dip(20)
                leftMargin = dip(8)
                rightMargin = dip(8)
            }

            linearLayout {
                textView(R.string.exit) {
                    btnExit = this
                    onClick {
                        owner.exitApp()
                    }
                }

                textView(R.string.download) {
                    btnDownload = this
                    onClick {
                        owner.startDownloading()
                    }
                }

                textView(R.string.cancel) {
                    btnCancel = this
                    visibility = View.GONE
                    onClick {
                        owner.cancelDownloading()
                    }
                }

                textView(R.string._continue) {
                    btnContinue = this
                    visibility = View.GONE
                    onClick {
                        owner.presenter.flowFinished()
                    }
                }

                textView(R.string.retry) {
                    btnRetry = this
                    visibility = View.GONE
                    onClick {
                        owner.startDownloading()
                    }
                }

                textView(R.string.in_background) {
                    btnInBackground = this
                    visibility = View.GONE
                    onClick {
                        Analytics.custom("Do in background")
                        owner.finish()
                        owner.synchronizeWithBackground()
                    }
                }
            }.applyRecursively { view ->
                when (view) {
                    is TextView -> with(view) {
                        backgroundResource = context.attrResource(R.attr.selectableItemBackground)
                        allCaps = true
                        textAppearance = R.style.TextAppearance_AppCompat_Body2
                        setPadding(dip(4), dip(12), dip(4), dip(12))
                        lparams { margin = dip(4) }
                    }
                }
            }.lparams(wrapContent, dip(52)) {
                gravity = Gravity.END
                setMargins(dip(0), dip(0), dip(8), dip(0))
            }
        }
    }
}
