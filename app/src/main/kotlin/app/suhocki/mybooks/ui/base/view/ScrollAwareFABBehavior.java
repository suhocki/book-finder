package app.suhocki.mybooks.ui.base.view;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class ScrollAwareFABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private static final String TAG = "ScrollAwareFABBehavior";

    public ScrollAwareFABBehavior() {
        super();
    }

    private boolean hidden = false;

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 0 && !hidden) {
            hidden = true;
            // User scrolled down -> hide the FAB
            Log.d(TAG, "onStartNestedScroll: animation is starting");
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();

            ViewCompat.animate(child).translationY(child.getHeight() + lp.bottomMargin).start();
        } else if (dyConsumed < 0 && hidden) {
            hidden = false;
            // User scrolled up -> show the FAB
            ViewCompat.animate(child).translationY(0).start();
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target,
                                       int axes, int type) {
        return target instanceof RecyclerView;
    }


}
