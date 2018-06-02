package app.suhocki.mybooks.ui.base.adapter.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class DividerItemDecoration extends ItemDecoration {

    private int mOffsets;
    private int mStartFromItem;



    /**
     * Create new DividerItemDecoration with the specified height and color
     * without additional offsets
     */
    public DividerItemDecoration(int dividerHeight, int startFromItem) {
        mOffsets = dividerHeight;
        mStartFromItem = startFromItem;
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