package com.bullhead.android.muftplacepicker;

import androidx.annotation.NonNull;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;

public class PlacePickerMapOptions implements Serializable {
    private final GeoPoint center;
    private final float    zoom;

    private PlacePickerMapOptions(@NonNull GeoPoint center,
                                  float zoom) {
        this.center = center;
        this.zoom   = zoom;
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public float getZoom() {
        return zoom;
    }

    @NonNull
    public GeoPoint getCenter() {
        return center;
    }

    public static class Builder {
        private GeoPoint center;
        private float    zoom;

        private Builder() {
        }

        @NonNull
        private GeoPoint lahore() {
            return new GeoPoint(31.582045, 74.329376);
        }

        @NonNull
        public Builder center(@NonNull GeoPoint point) {
            this.center = point;
            return this;
        }

        @NonNull
        public Builder zoom(float zoom) {
            this.zoom = zoom;
            return this;
        }

        public PlacePickerMapOptions build() {
            if (center == null) {
                center = lahore();
            }
            if (zoom == 0) {
                zoom = 15;
            }
            return new PlacePickerMapOptions(center, zoom);
        }
    }
}
