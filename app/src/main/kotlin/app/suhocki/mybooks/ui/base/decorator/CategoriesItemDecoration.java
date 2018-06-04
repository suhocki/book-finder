package app.suhocki.mybooks.ui.base.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import app.suhocki.mybooks.ui.catalog.CatalogFragment;

public class CategoriesItemDecoration extends ItemDecoration {

    private int mOffsets;
    private int mStartFromItem;

    public CategoriesItemDecoration(int dividerHeight) {
        mOffsets = dividerHeight;
        mStartFromItem = CatalogFragment.CATEGORY_POSITION;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               State state) {

        LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager == null){
            throw new RuntimeException("LayoutManager not found");
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = layoutParams.getViewAdapterPosition();
        if(layoutManager.getPosition(view) != 0 && itemPosition > mStartFromItem)
            outRect.top = mOffsets;
    }
}