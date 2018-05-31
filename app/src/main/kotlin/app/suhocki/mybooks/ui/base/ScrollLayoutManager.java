package app.suhocki.mybooks.ui.base;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class ScrollLayoutManager extends LinearLayoutManager {
    private static final float MILLISECONDS_PER_INCH = 50f;
    private Context mContext;

    public ScrollLayoutManager(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate) {
        return false;
    }

    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate, boolean focusedChildVisible) {
        return false;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView,
                                       RecyclerView.State state, final int position) {

        LinearSmoothScroller smoothScroller =
                new LinearSmoothScroller(mContext) {

                    //This controls the direction in which smoothScroll looks
                    //for your view
                    @Override
                    public PointF computeScrollVectorForPosition
                    (int targetPosition) {
                        return ScrollLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    //This returns the milliseconds it takes to
                    //scroll one pixel.
                    @Override
                    protected float calculateSpeedPerPixel
                    (DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
                    }

                    @Override
                    protected void onStop() {
                        super.onStop();
                        Log.d("ahahah", "stopped");
                    }
                };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
