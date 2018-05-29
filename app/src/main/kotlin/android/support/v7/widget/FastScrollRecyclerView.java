package android.support.v7.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import app.suhocki.mybooks.R;

public class FastScrollRecyclerView extends RecyclerView {

    private static final int NOT_INITIALIZED = -1;

    private int fastScrollThickness = NOT_INITIALIZED;
    private int fastScrollMinimumHeight = NOT_INITIALIZED;
    private int fastScrollMinimumRange = NOT_INITIALIZED;
    private int fastScrollMargin = NOT_INITIALIZED;

    public FastScrollRecyclerView(Context context) {
        super(context);
    }

    @SuppressLint("VisibleForTests")
    @SuppressWarnings("ConstantConditions")
    public void enableFastScroller(
            @DrawableRes int verticalThumbDrawableRes,
            @DrawableRes int verticalTrackDrawableRes,
            @DrawableRes int horizontalThumbDrawableRes,
            @DrawableRes int horizontalTrackDrawableRes
    ) {
        Context context = getContext();
        StateListDrawable verticalThumbDrawable = (StateListDrawable) ContextCompat.getDrawable(context, verticalThumbDrawableRes);
        Drawable verticalTrackDrawable = ContextCompat.getDrawable(context, verticalTrackDrawableRes);
        StateListDrawable horizontalThumbDrawable = (StateListDrawable) ContextCompat.getDrawable(context, horizontalThumbDrawableRes);
        Drawable horizontalTrackDrawable = ContextCompat.getDrawable(context, horizontalTrackDrawableRes);
        if (verticalThumbDrawable == null || verticalTrackDrawable == null
                || horizontalThumbDrawable == null || horizontalTrackDrawable == null) {
            throw new IllegalArgumentException(
                    "Trying to set fast scroller without both required drawables." + exceptionLabel());
        }

        Resources resources = getContext().getResources();

        int fsThickness = isInitialized(fastScrollThickness) ? fastScrollThickness :
                resources.getDimensionPixelSize(R.dimen.fastscroll_default_thickness);

        int fsMinimumRange = isInitialized(fastScrollMinimumRange) ? fastScrollMinimumRange :
                resources.getDimensionPixelSize(R.dimen.fastscroll_minimum_range);

        int fsMargin = isInitialized(fastScrollMargin) ? fastScrollMargin :
                resources.getDimensionPixelOffset(R.dimen.fastscroll_margin);

        new MinHeightFastScroller(this, verticalThumbDrawable, verticalTrackDrawable,
                horizontalThumbDrawable, horizontalTrackDrawable,
                fsThickness, fsMinimumRange, fsMargin, fastScrollMinimumHeight);
    }

    public FastScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FastScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static int getNotInitialized() {
        return NOT_INITIALIZED;
    }

    public int getFastScrollThickness() {
        return fastScrollThickness;
    }

    public void setFastScrollThickness(int fastScrollThickness) {
        this.fastScrollThickness = fastScrollThickness;
    }

    public int getFastScrollMinimumRange() {
        return fastScrollMinimumRange;
    }

    public void setFastScrollMinimumRange(int fastScrollMinimumRange) {
        this.fastScrollMinimumRange = fastScrollMinimumRange;
    }

    public int getFastScrollMargin() {
        return fastScrollMargin;
    }

    public void setFastScrollMargin(int fastScrollMargin) {
        this.fastScrollMargin = fastScrollMargin;
    }

    public int getFastScrollMinimumHeight() {
        return fastScrollMinimumHeight;
    }

    public void setFastScrollMinimumHeight(int fastScrollMinimumHeight) {
        this.fastScrollMinimumHeight = fastScrollMinimumHeight;
    }

    private boolean isInitialized(int value) {
        return value != NOT_INITIALIZED;
    }
}