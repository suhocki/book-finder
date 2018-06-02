package app.suhocki.mybooks.ui.base.adapter.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import app.suhocki.mybooks.ui.catalog.CatalogFragment;

public class SearchItemDecoration extends ItemDecoration {

    private int mOffsets;
    private int mStartFromItem;

    public SearchItemDecoration(int dividerHeight) {
        mOffsets = dividerHeight;
        mStartFromItem = CatalogFragment.SEARCH_RESULT_POSITION;
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
        if(layoutManager.getPosition(view) != 0 && itemPosition >= mStartFromItem) {
            outRect.top = mOffsets;
        }
        int adapterItemCount = parent.getAdapter().getItemCount();
        if (itemPosition == adapterItemCount - 1) {
            outRect.bottom = mOffsets;
        }
    }
}