package com.bullhead.android.muftplacepicker.ui.search;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bullhead.android.muftplacepicker.domain.Place;
import com.bullhead.android.muftplacepicker.ui.base.BaseRecyclerAdapter;

import java.util.List;

public class SearchAdapter extends BaseRecyclerAdapter<Place, SearchItemViewHolder> {
    public SearchAdapter(@NonNull List<Place> items) {
        super(items);
    }

    @NonNull
    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SearchItemViewHolder.create(parent);
    }
}
