package app.suhocki.mybooks.presentation.catalog.adapter

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.domain.model.CatalogItem
import app.suhocki.mybooks.domain.model.Category
import app.suhocki.mybooks.presentation.catalog.adapter.model.BannersTypedItem
import app.suhocki.mybooks.presentation.catalog.adapter.ui.BannersItemUI
import app.suhocki.mybooks.presentation.catalog.adapter.ui.CategoryItemUI
import app.suhocki.mybooks.presentation.catalog.adapter.ui.HeaderCatalogItemUI
import app.suhocki.mybooks.presentation.catalog.adapter.ui.SearchItemUI
import app.suhocki.mybooks.presentation.catalog.adapter.viewholder.BannersViewHolder
import app.suhocki.mybooks.presentation.catalog.adapter.viewholder.CategoryViewHolder
import app.suhocki.mybooks.presentation.catalog.adapter.viewholder.HeaderCatalogViewHolder
import app.suhocki.mybooks.presentation.catalog.adapter.viewholder.SearchViewHolder
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoContextImpl
import org.jetbrains.anko.AnkoLogger

class CatalogAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AnkoLogger {

    private var differ = AsyncListDiffer(this, CatalogDiffCallback())

    private lateinit var onCategoryClickListener: OnCategoryClickListener

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
        when (position) {
            0 -> {
                val bannersTypedItem = differ.currentList[position] as BannersTypedItem
                val layout = (holder as BannersViewHolder).layout
                Picasso.get().load(bannersTypedItem.banners.first().pictureUrl)
                    .into(layout.image)
                layout.description.text = bannersTypedItem.banners.first().text
            }
            1, 2 -> {
            }
            else -> (holder as CategoryViewHolder).layout.category =
                    differ.currentList[position] as Category
        }
    }

    fun submitList(list: List<CatalogItem>) =
        mutableListOf<CatalogItem>().apply {
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