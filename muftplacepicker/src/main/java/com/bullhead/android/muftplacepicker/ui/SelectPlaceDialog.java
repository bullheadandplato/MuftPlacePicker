package com.bullhead.android.muftplacepicker.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.bullhead.android.muftplacepicker.databinding.DialogSelectPlaceBinding;
import com.bullhead.android.muftplacepicker.domain.Place;
import com.bullhead.android.muftplacepicker.util.StaticImageProvider;
import com.bumptech.glide.Glide;

import org.osmdroid.util.GeoPoint;

public class SelectPlaceDialog extends Dialog {
    private DialogSelectPlaceBinding binding;

    public SelectPlaceDialog(@NonNull Context context,
                             @NonNull Place place,
                             @NonNull Consumer<Place> onDone) {
        super(context);
        setCancelable(false);
        if (this.getWindow() != null) {
            this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        binding = DialogSelectPlaceBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());
        binding.addressTextView.setText(place.formattedAddress());
        binding.nameTextView.setText(place.getDisplayName());
        GeoPoint location = place.location();
        if (location != null) {
            Glide.with(binding.imageView)
                    .load(StaticImageProvider.get(location))
                    .centerCrop()
                    .into(binding.imageView);
        }
        binding.cancelButton.setOnClickListener(v -> dismiss());
        binding.okButton.setOnClickListener(v -> {
            dismiss();
            onDone.accept(place);
        });
    }
}
