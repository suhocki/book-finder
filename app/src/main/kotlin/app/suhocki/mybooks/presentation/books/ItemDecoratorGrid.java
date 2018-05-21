package app.suhocki.mybooks.presentation.books;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by khmialeuski_vitali on 13.07.17.
 */

public class ItemDecoratorGrid extends RecyclerView.ItemDecoration {

    private final int offset;

    public ItemDecoratorGrid(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int spanCount = layoutManager.getSpanCount();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = layoutParams.getViewAdapterPosition();
        outRect.right = (itemPosition + 1) % spanCount == 0 ? offset : 0;
        outRect.top = offset;
        outRect.left = offset;
        int nearBottomViewCount = layoutManager.getItemCount() % spanCount;
        if (nearBottomViewCount == 0) {
            nearBottomViewCount = spanCount;
        }
        int lastPositions = layoutManager.getItemCount() - nearBottomViewCount;
        outRect.bottom = itemPosition >= lastPositions ? offset : 0;
    }
}
