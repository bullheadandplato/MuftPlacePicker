package com.bullhead.android.osm.placepicker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bullhead.android.muftplacepicker.PlacePicker;
import com.bullhead.android.muftplacepicker.PlacePickerMapOptions;
import com.bullhead.android.muftplacepicker.PlacePickerUiOptions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                            getSupportFragmentManager());
                });

    }
}