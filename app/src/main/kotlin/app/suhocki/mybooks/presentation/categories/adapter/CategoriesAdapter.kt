package app.suhocki.mybooks.presentation.categories.adapter

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import app.suhocki.mybooks.di.DI
import app.suhocki.mybooks.domain.model.Category
import org.jetbrains.anko.AnkoContextImpl
import org.jetbrains.anko.AnkoLogger
import toothpick.Toothpick
import javax.inject.Inject

class CategoriesAdapter @Inject constructor() :
    RecyclerView.Adapter<CategoryViewHolder>(), AnkoLogger {

    private lateinit var differ: AsyncListDiffer<Category>
    private lateinit var onCategoryClickListener: OnCategoryClickListener

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (!this::differ.isInitialized) differ =
                Toothpick.openScopes(DI.APP_SCOPE, DI.CATEGORIES_ACTIVITY_SCOPE)
                    .getInstance(CategoriesDiffer::class.java).get()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val viewHolder = CategoryViewHolder(CategoryItemUI().apply {
            createView(AnkoContextImpl(parent.context, parent, false))
        })

        viewHolder.itemView.setOnClickListener {
            val category = differ.currentList[viewHolder.adapterPosition]
            onCategoryClickListener.onCategoryClick(category)
        }
        return viewHolder

    }

    override fun getItemCount(): Int =
        differ.currentList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.layout.category = differ.currentList[position]
    }

    fun submitList(list: List<Category>) =
        mutableListOf<Category>().apply {
            addAll(list)
            differ.submitList(this)
        }

    fun setOnCategoryClickListener(onCategoryClickListener: OnCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener
    }
}