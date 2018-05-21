package app.suhocki.mybooks.presentation.books

import android.graphics.Color
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import app.suhocki.mybooks.R
import app.suhocki.mybooks.presentation.books.adapter.BooksAdapter
import app.suhocki.mybooks.themedAutofitRecyclerView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.tintedImageView
import javax.inject.Inject


class BooksUI @Inject constructor(
    private var adapter: BooksAdapter
) : AnkoComponent<BooksActivity> {

    lateinit var progressBar: ProgressBar
    lateinit var emptyView: View

    override fun createView(ui: AnkoContext<BooksActivity>) = with(ui) {
        frameLayout {
            themedAutofitRecyclerView(R.style.ScrollbarRecyclerView) {
                id = R.id.id_recycler_books
                clipToPadding = false
                setHasFixedSize(true)
                adapter = this@BooksUI.adapter
                columnWidth = dip(146)
                addItemDecoration(ItemDecoratorGrid(dip(8)))
            }

            themedProgressBar(R.style.ColoredProgressBar) {
                progressBar = this
                visibility = View.GONE
            }.lparams {
                gravity = Gravity.CENTER
            }

            verticalLayout {
                emptyView = this
                gravity = Gravity.CENTER
                visibility = View.GONE

                tintedImageView(R.drawable.ic_info).apply {
                    DrawableCompat.wrap(drawable).apply {
                        DrawableCompat.setTint(this, Color.GRAY)
                        setImageDrawable(this)
                    }

                    textView(R.string.empty_screen) {
                        gravity = Gravity.CENTER
                    }.lparams {
                        topMargin = dip(16)
                        bottomMargin = dip(16)
                    }
                }
            }.lparams {
                gravity = Gravity.CENTER
                rightMargin = dip(56)
                leftMargin = dip(56)
            }
        }
    }
}