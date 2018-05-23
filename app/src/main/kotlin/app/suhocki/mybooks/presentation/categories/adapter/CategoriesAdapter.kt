package app.suhocki.mybooks.presentation.categories.adapter

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.domain.model.TypedItem
import app.suhocki.mybooks.presentation.categories.adapter.ui.BannersItemUI
import app.suhocki.mybooks.presentation.categories.adapter.ui.CategoryItemUI
import app.suhocki.mybooks.presentation.categories.adapter.ui.HeaderCatalogItemUI
import app.suhocki.mybooks.presentation.categories.adapter.ui.SearchItemUI
import app.suhocki.mybooks.presentation.categories.adapter.viewholder.BannersViewHolder
import app.suhocki.mybooks.presentation.categories.adapter.viewholder.CategoryViewHolder
import app.suhocki.mybooks.presentation.categories.adapter.viewholder.HeaderCatalogViewHolder
import app.suhocki.mybooks.presentation.categories.adapter.viewholder.SearchViewHolder
import org.jetbrains.anko.AnkoContextImpl
import org.jetbrains.anko.AnkoLogger
import toothpick.Toothpick
import javax.inject.Inject

class CategoriesAdapter @Inject constructor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), AnkoLogger {

    private lateinit var differ: AsyncListDiffer<TypedItem>
    private lateinit var onCategoryClickListener: OnCategoryClickListener

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (!this::differ.isInitialized) differ =
                Toothpick.openScopes(DI.APP_SCOPE, DI.CATEGORIES_ACTIVITY_SCOPE)
                    .getInstance(CategoriesDiffer::class.java).get()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            VIEW_TYPE_CATEGORY -> {
                viewHolder = CategoryViewHolder(CategoryItemUI().apply {
                    createView(AnkoContextImpl(parent.context, parent, false))
                })
                viewHolder.itemView.setOnClickListener {
                    val category = differ.currentList[viewHolder.adapterPosition] as Category
                    onCategoryClickListener.onCategoryClick(category)
                }
            }
            VIEW_TYPE_HEADER_CATALOG -> {
                viewHolder = HeaderCatalogViewHolder(HeaderCatalogItemUI().apply {
                    createView(AnkoContextImpl(parent.context, parent, false))
                })
            }
            VIEW_TYPE_SEARCH -> {
                viewHolder = SearchViewHolder(SearchItemUI().apply {
                    createView(AnkoContextImpl(parent.context, parent, false))
                })
            }
            VIEW_TYPE_BANNERS -> {
                viewHolder = BannersViewHolder(BannersItemUI().apply {
                    createView(AnkoContextImpl(parent.context, parent, false))
                })
            }
            else -> throw Exception()
        }
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_BANNERS
            1 -> VIEW_TYPE_SEARCH
            2 -> VIEW_TYPE_HEADER_CATALOG
            else -> VIEW_TYPE_CATEGORY
        }
    }

    override fun getItemCount(): Int =
        differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position > 2) (holder as CategoryViewHolder).layout.category =
                differ.currentList[position] as Category
    }

    fun submitList(list: List<TypedItem>) =
        mutableListOf<TypedItem>().apply {
            addAll(list)
            differ.submitList(this)
        }

    fun setOnCategoryClickListener(onCategoryClickListener: OnCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener
    }

    companion object {
        private const val VIEW_TYPE_BANNERS = 0
        private const val VIEW_TYPE_SEARCH = 1
        private const val VIEW_TYPE_HEADER_CATALOG = 2
        private const val VIEW_TYPE_CATEGORY = 3
    }
}