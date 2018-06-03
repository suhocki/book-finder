package app.suhocki.mybooks.ui.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ScrollAwareFABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton>  {

    private static final String TAG = "ScrollAwareFABBehavior";

    private boolean fabAnimationStarted = false;
    private boolean flingHappened = false;

    public ScrollAwareFABBehavior() {
        super();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {

        if (target instanceof RecyclerView) {
            return true;
        }
        return false;
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull final FloatingActionButton child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);

        // If animation didn't start, we don't need to care about running the restore animation.
        // i.e.: when the user swipes to another tab in a viewpager. The onNestedPreScroll is never called.
        if (!fabAnimationStarted) {
            return;
        }

        // Animate back when the fling ended (TYPE_NON_TOUCH)
        // or if the user made the touch up (TYPE_TOUCH) but the fling didn't happen.
        if (type == ViewCompat.TYPE_NON_TOUCH || (type == ViewCompat.TYPE_TOUCH && !flingHappened)) {
            ViewCompat.animate(child).translationY(0).start();

            fabAnimationStarted = false;
        }
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {

        // We got a fling. Flag it.
        flingHappened = true;
        return false;

    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

        if (!fabAnimationStarted) {
            Log.d(TAG, "onStartNestedScroll: animation is starting");
            fabAnimationStarted = true;
            flingHappened = false;
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();

            ViewCompat.animate(child).translationY(child.getHeight() + lp.bottomMargin).start();

        }
    }
}
