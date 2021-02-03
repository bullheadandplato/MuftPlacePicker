package com.bullhead.android.muftplacepicker.api;

import com.bullhead.android.muftplacepicker.domain.Place;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NominatimService {
    @GET("reverse?format=jsonv2")
    Single<Place> reverse(@Query("lat") double lat, @Query("lon") double lng);

    @GET("search?format=jsonv2")
    Single<List<Place>> search(@Query("q") String query);
}
