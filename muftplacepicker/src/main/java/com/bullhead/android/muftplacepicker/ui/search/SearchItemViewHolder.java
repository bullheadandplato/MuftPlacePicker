package com.bullhead.android.muftplacepicker.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bullhead.android.muftplacepicker.R;
import com.bullhead.android.muftplacepicker.databinding.ItemSearchPlaceBinding;
import com.bullhead.android.muftplacepicker.domain.Place;
import com.bullhead.android.muftplacepicker.ui.base.BaseViewHolder;

public class SearchItemViewHolder extends BaseViewHolder<Place> {
    private final ItemSearchPlaceBinding binding;

    private SearchItemViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemSearchPlaceBinding.bind(itemView);
    }

    @NonNull
    public static SearchItemViewHolder create(@NonNull ViewGroup parent) {
        return new SearchItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_place, parent, false));
    }

    @Override
    public <X extends Place> void bind(@NonNull X t) {
        binding.nameTextView.setText(t.getName());
        binding.addressTextView.setText(t.formattedAddress());
    }
}
