package com.bullhead.android.muftplacepicker.util;

import androidx.annotation.NonNull;

import org.osmdroid.util.GeoPoint;

import java.util.Locale;

public final class StaticImageProvider {
    //https://github.com/jperelli/osm-static-maps
    private static final String URL = "http://osm-static-maps.herokuapp.com/?" +
            "geojson={\"type\":\"Point\",\"coordinates\":[%f,%f]}&height=200&width=250";

    private StaticImageProvider() {
        throw new AssertionError("Not instantiable");
    }

    @NonNull
    public static String get(@NonNull GeoPoint point) {
        return String.format(Locale.ENGLISH, URL, point.getLongitude(), point.getLatitude())
                .replace("\"", "%22"); //replace " with encoded char for url
    }
}
