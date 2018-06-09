package android.support.v7.recyclerview.extensions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.EndActionDiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class EndActionAsyncListDiffer<T> {
    private final ListUpdateCallback mUpdateCallback;
    private final EndActionAsyncDifferConfig<T> mConfig;

    /**
     * Convenience for
     * {@code AsyncListDiffer(new AdapterListUpdateCallback(adapter),
     * new AsyncDifferConfig.Builder().setDiffCallback(diffCallback).build());}
     *
     * @param adapter Adapter to dispatch position updates to.
     * @param diffCallback ItemCallback that compares items to dispatch appropriate animations when
     *
     * @see DiffUtil.DiffResult#dispatchUpdatesTo(RecyclerView.Adapter)
     */
    public EndActionAsyncListDiffer(@NonNull RecyclerView.Adapter adapter,
                                    @NonNull EndActionDiffUtil.ItemCallback<T> diffCallback) {
        mUpdateCallback = new AdapterListUpdateCallback(adapter);
        mConfig = new EndActionAsyncDifferConfig.Builder<>(diffCallback).build();
    }

    /**
     * Create a AsyncListDiffer with the provided config, and ListUpdateCallback to dispatch
     * updates to.
     *
     * @param listUpdateCallback Callback to dispatch updates to.
     * @param config Config to define background work Executor, and DiffUtil.ItemCallback for
     *               computing List diffs.
     *
     * @see DiffUtil.DiffResult#dispatchUpdatesTo(RecyclerView.Adapter)
     */
    @SuppressWarnings("WeakerAccess")
    public EndActionAsyncListDiffer(@NonNull ListUpdateCallback listUpdateCallback,
                                    @NonNull EndActionAsyncDifferConfig<T> config) {
        mUpdateCallback = listUpdateCallback;
        mConfig = config;
    }

    @Nullable
    private List<T> mList;

    /**
     * Non-null, unmodifiable version of mList.
     * <p>
     * Collections.emptyList when mList is null, wrapped by Collections.unmodifiableList otherwise
     */
    @NonNull
    private List<T> mReadOnlyList = Collections.emptyList();

    // Max generation of currently scheduled runnable
    private int mMaxScheduledGeneration;

    /**
     * Get the current List - any diffing to present this list has already been computed and
     * dispatched via the ListUpdateCallback.
     * <p>
     * If a <code>null</code> List, or no List has been submitted, an empty list will be returned.
     * <p>
     * The returned list may not be mutated - mutations to content must be done through
     * {@link #submitList(List)}.
     *
     * @return current List.
     */
    @NonNull
    public List<T> getCurrentList() {
        return mReadOnlyList;
    }

    /**
     * Pass a new List to the AdapterHelper. Adapter updates will be computed on a background
     * thread.
     * <p>
     * If a List is already present, a diff will be computed asynchronously on a background thread.
     * When the diff is computed, it will be applied (dispatched to the {@link ListUpdateCallback}),
     * and the new List will be swapped in.
     *
     * @param newList The new List.
     */
    @SuppressWarnings("WeakerAccess")
    public void submitList(final List<T> newList) {
        if (newList == mList) {
            // nothing to do
            return;
        }

        // incrementing generation means any currently-running diffs are discarded when they finish
        final int runGeneration = ++mMaxScheduledGeneration;

        // fast simple remove all
        if (newList == null) {
            //noinspection ConstantConditions
            int countRemoved = mList.size();
            mList = null;
            mReadOnlyList = Collections.emptyList();
            // notify last, after list is updated
            mUpdateCallback.onRemoved(0, countRemoved);
            return;
        }

        // fast simple first insert
        if (mList == null) {
            mList = newList;
            mReadOnlyList = Collections.unmodifiableList(newList);
            // notify last, after list is updated
            mUpdateCallback.onInserted(0, newList.size());
            return;
        }

        final List<T> oldList = mList;
        mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                final EndActionDiffUtil.DiffResult result = EndActionDiffUtil.calculateDiff(new EndActionDiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return oldList.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return newList.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        return mConfig.getDiffCallback().areItemsTheSame(
                                oldList.get(oldItemPosition), newList.get(newItemPosition));
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        return mConfig.getDiffCallback().areContentsTheSame(
                                oldList.get(oldItemPosition), newList.get(newItemPosition));
                    }

                    @Nullable
                    @Override
                    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                        return mConfig.getDiffCallback().getChangePayload(
                                oldList.get(oldItemPosition), newList.get(newItemPosition));
                    }
                });

                mConfig.getMainThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mMaxScheduledGeneration == runGeneration) {
                            latchList(newList, result);
                        }
                    }
                });
            }
        });
    }

    private void latchList(@NonNull List<T> newList, @NonNull EndActionDiffUtil.DiffResult diffResult) {
        mList = newList;
        // notify last, after list is updated
        mReadOnlyList = Collections.unmodifiableList(newList);
        diffResult.dispatchUpdatesTo(mUpdateCallback);
    }
}
