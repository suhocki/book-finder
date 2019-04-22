package app.suhocki.mybooks.ui.search

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import app.suhocki.mybooks.R
import app.suhocki.mybooks.presentation.global.GlobalMenuController
import app.suhocki.mybooks.ui.Ids
import app.suhocki.mybooks.ui.base.BaseFragment
import app.suhocki.mybooks.ui.base.materialCardView
import app.suhocki.mybooks.ui.base.themedToolbarCompat
import app.suhocki.mybooks.ui.base.view.ScrollLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.themedRecyclerView
import toothpick.Toothpick
import javax.inject.Inject

class SearchFragment : BaseFragment<SearchFragment.SearchUI>(), SearchView {

    @InjectPresenter
    lateinit var presenter: SearchPresenter

    @ProvidePresenter
    fun providePresenter(): SearchPresenter =
        scope.getInstance(SearchPresenter::class.java)

    @Inject
    lateinit var menuController: GlobalMenuController

    override val ui by lazy {
        SearchUI()
    }

    private val adapter by lazy {
        SearchAdapter(
            SearchAdapter.SearchDiffCallback(),
            {}
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ui.toolbar.setNavigationOnClickListener {
            val animatedDrawable = ui.toolbar.navigationIcon as DrawerArrowDrawable
            val isArrowVisible = animatedDrawable.progress > 0f

            if (isArrowVisible) {
            } else {
                menuController.open()
            }
        }
        ui.recyclerView.adapter = adapter
        ui.recyclerView.layoutManager = ScrollLayoutManager(context)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    override fun showData(data: List<Any>) {
        adapter.setData(data)
    }

    inner class SearchUI : AnkoComponent<Context> {
        lateinit var parent: View
        lateinit var toolbar: Toolbar
        lateinit var recyclerView: RecyclerView
        lateinit var progressBar: View

        val colorWhite by lazy {
            ResourcesCompat.getColor(resources, R.color.colorWhite, requireActivity().theme)
        }
        val colorBlack by lazy {
            ResourcesCompat.getColor(resources, R.color.colorBlack, requireActivity().theme)
        }

        override fun createView(ui: AnkoContext<Context>) =
            ui.coordinatorLayout {
                this@SearchUI.parent = this
                backgroundColorResource = R.color.colorWhite

                themedAppBarLayout(R.style.ThemeOverlay_AppCompat_Light) {

                    themedToolbarCompat(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                        toolbar = this

                        navigationIcon = DrawerArrowDrawable(context!!)
                            .apply { color = Color.WHITE }

                        setContentInsetsRelative(0, 0)

                        imageView(R.drawable.logo) {
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            layoutParams = Toolbar.LayoutParams(
                                Toolbar.LayoutParams.WRAP_CONTENT,
                                Toolbar.LayoutParams.WRAP_CONTENT
                            ).apply {
                                margin = dip(8)
                                gravity = Gravity.CENTER_HORIZONTAL
                            }
                        }
                    }.lparams(matchParent, dimenAttr(R.attr.actionBarSize)) {
                        scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                                AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                    }

                    frameLayout {
                        materialCardView {
                            setCardBackgroundColor(colorWhite)
                            radius = dip(10).toFloat()
                            useCompatPadding = true

                            editText {
                                id = Ids.search
                                backgroundColorResource = android.R.color.transparent
                                horizontalPadding = dip(12)
                                textSizeDimen = R.dimen.size_text_search
                                maxLines = 1
                                isFocusable = true
                                isFocusableInTouchMode = true
                                inputType = InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
                                imeOptions = EditorInfo.IME_ACTION_SEARCH
                                isVerticalScrollBarEnabled = false
                                hintResource = R.string.hint_search
                            }
                        }.lparams(matchParent, dimenAttr(R.attr.actionBarSize)) {
                            verticalPadding = dimen(R.dimen.padding_item_search)
                            horizontalMargin = dip(4)
                        }
                    }.lparams(matchParent, matchParent) {
                        scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                    }

                }.lparams(matchParent, wrapContent)

                themedRecyclerView(R.style.ScrollbarRecyclerView) {
                    recyclerView = this
                    id = Ids.recyclerSearch
                    clipToPadding = false
                }.lparams(matchParent, matchParent) {
                    behavior = AppBarLayout.ScrollingViewBehavior()
                }

                themedProgressBar(R.style.AccentProgressBar) {
                    progressBar = this
                    visibility = View.GONE
                    topPadding = dimenAttr(R.attr.actionBarSize)
                }.lparams {
                    gravity = Gravity.CENTER
                }
            }
    }
}