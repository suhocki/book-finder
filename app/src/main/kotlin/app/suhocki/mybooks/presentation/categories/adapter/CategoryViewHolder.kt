package app.suhocki.mybooks.presentation.categories.adapter

import android.support.v7.widget.RecyclerView
import app.suhocki.mybooks.domain.model.Category


class CategoryViewHolder(
    private var categoryItemLayout: CategoryItemUI
) : RecyclerView.ViewHolder(categoryItemLayout.parent) {

    fun bind(category: Category) {
        categoryItemLayout.name.text = category.name
    }
}