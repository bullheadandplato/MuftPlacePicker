package com.bullhead.android.muftplacepicker.ui.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("unused")
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    protected OnItemClick listener;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(getAdapterPosition());
            }
        });
    }

    @NonNull
    public String getString(@StringRes int res, Object... args) {
        if (itemView.getContext() != null) {
            return itemView.getContext().getString(res, args);
        }
        return "";
    }

    public abstract <X extends T> void bind(@NonNull X t);

    public void toggleProgress(boolean show) {
        //ignore
    }

    public void setListener(@Nullable OnItemClick listener) {
        this.listener = listener;
    }

    public interface OnItemClick {
        void onClick(int position);
    }
}
