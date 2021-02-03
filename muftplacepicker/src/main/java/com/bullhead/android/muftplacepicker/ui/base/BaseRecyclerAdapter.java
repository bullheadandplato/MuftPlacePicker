package com.bullhead.android.muftplacepicker.ui.base;

import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T, VH extends BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {
    private static final String TAG = BaseRecyclerAdapter.class.getSimpleName();

    protected final List<T>        items;
    private final   List<Integer>  progressPositions;
    protected       OnItemClick<T> listener;

    protected BaseRecyclerAdapter(@NonNull List<T> items) {
        this.items        = new ArrayList<>(items);
        progressPositions = new ArrayList<>();
    }

    public void update(@NonNull List<T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setListener(@Nullable OnItemClick<T> listener) {
        this.listener = listener;
    }

    public void toggleProgress(int position) {
        if (progressPositions.contains(position)) {
            progressPositions.remove(Integer.valueOf(position));
        } else {
            progressPositions.add(position);
        }
        notifyItemChanged(position);
    }

    @Override
    @CallSuper
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.setListener(this::onHolderClick);
        holder.bind(items.get(position));
        holder.toggleProgress(progressPositions.contains(position));
    }

    private void onHolderClick(int position) {
        if (listener != null) {
            if (position > -1 && position < items.size()) {
                listener.onClick(items.get(position), position);
            }
        } else {
            Log.i(TAG, "onHolderClick: listener is not attached");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public interface OnItemClick<T> {
        void onClick(@NonNull T item, int position);
    }
}
