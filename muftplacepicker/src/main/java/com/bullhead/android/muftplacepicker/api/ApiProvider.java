package com.bullhead.android.muftplacepicker.api;

import androidx.annotation.NonNull;

import com.bullhead.android.muftplacepicker.domain.Place;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiProvider {
    private static final ApiProvider instance;

    static {
        instance = new ApiProvider();
    }


    private final NominatimService service;

    private ApiProvider() {
        service = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://nominatim.openstreetmap.org/")
                .build()
                .create(NominatimService.class);
    }

    public static ApiProvider getInstance() {
        return instance;
    }

    @NonNull
    public Single<List<Place>> search(@NonNull String query) {
        return service.search(query)
                .flatMap(places -> {
                    if (places.size() > 5) {
                        return Single.just(places.subList(0, 5));
                    } else if (places.size() == 0) {
                        return Single.error(new Exception("no result for " + query));
                    }
                    return Single.just(places);
                });
    }

    @NonNull
    public Single<String> reverseAddress(double lat, double lng) {
        return service.reverse(lat, lng)
                .flatMap(reverseResponse -> {
                    if (reverseResponse.getDisplayName() != null) {
                        return Single.just(reverseResponse.getDisplayName());
                    }
                    return Single.error(new Exception("No address found"));
                });
    }


}
