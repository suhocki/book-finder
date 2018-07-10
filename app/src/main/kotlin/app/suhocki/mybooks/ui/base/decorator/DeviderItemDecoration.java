package app.suhocki.mybooks.ui.base.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

import java.util.List;

import app.suhocki.mybooks.domain.model.Header;
import app.suhocki.mybooks.ui.info.InfoAdapter;

public class DeviderItemDecoration extends ItemDecoration {

    private int mOffsets;
    private int mStartFromItem;
    private boolean mIsInfoScreen;

    public DeviderItemDecoration(int dividerHeight, int startFrom) {
        mOffsets = dividerHeight;
        mStartFromItem = startFrom;
    }

    public DeviderItemDecoration(int dividerHeight, boolean isInfoScreen) {
        mOffsets = dividerHeight;
        mIsInfoScreen = isInfoScreen;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               State state) {

        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager == null) {
            throw new RuntimeException("LayoutManager not found");
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = layoutParams.getViewAdapterPosition();

        if (mIsInfoScreen) {
            List<Object> items = ((InfoAdapter) parent.getAdapter()).getItems();
            if (items.get(itemPosition) instanceof Header) {
                outRect.top = mOffsets;
            }
        } else if (itemPosition > mStartFromItem) {
            outRect.top = mOffsets;
        }

    }
}