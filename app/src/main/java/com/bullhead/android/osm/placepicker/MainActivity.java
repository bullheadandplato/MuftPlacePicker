package com.bullhead.android.osm.placepicker;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bullhead.android.muftplacepicker.PlacePickerMapOptions;
import com.bullhead.android.muftplacepicker.PlacePickerUiOptions;
import com.bullhead.android.muftplacepicker.ui.PlacePicker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView placeTextView = findViewById(R.id.placeTextView);
        findViewById(R.id.placePickerButton)
                .setOnClickListener(v -> {
                    PlacePicker.show(PlacePickerUiOptions.builder()
                                    .primaryColor(R.color.purple_700)
                                    .secondaryColor(R.color.white)
                                    .searchTextColor(R.color.black)
                                    .build(),
                            PlacePickerMapOptions.builder()
                                    .zoom(16)
                                    .build(),
                            place -> {
                                placeTextView.setText(place.getDisplayName());
                            },
                            getSupportFragmentManager());
                });

    }
}