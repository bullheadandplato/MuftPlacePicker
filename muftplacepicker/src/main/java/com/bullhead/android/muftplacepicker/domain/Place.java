package com.bullhead.android.muftplacepicker.domain;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Place implements Serializable {
    @SerializedName("place_id")
    private long         placeId;
    @SerializedName("licence")
    private String       licence;
    @SerializedName("osm_type")
    private String       osmType;
    @SerializedName("osm_id")
    private long         osmId;
    @SerializedName("lat")
    private String       lat;
    @SerializedName("lon")
    private String       lon;
    @SerializedName("place_rank")
    private long         placeRank;
    @SerializedName("category")
    private String       category;
    @SerializedName("type")
    private String       type;
    @SerializedName("importance")
    private double       importance;
    @SerializedName("addresstype")
    private String       addressType;
    @SerializedName("name")
    private String       name;
    @SerializedName("display_name")
    private String       displayName;
    @SerializedName("address")
    private Address      address;
    @SerializedName("boundingbox")
    private List<String> boundingXox = null;

    public String getName() {
        if (TextUtils.isEmpty(name)) {
            return displayName;
        }
        return name;
    }

    public String formattedAddress() {
        if (address == null) {
            return displayName;
        }
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(address.getPostcode())) {
            builder.append(address.getPostcode());
        }
        if (!TextUtils.isEmpty(address.getRoad())) {
            builder.append(",")
                    .append(address.getRoad());
        }
        if (!TextUtils.isEmpty(address.getStateDistrict())) {
            builder.append(",")
                    .append(address.getStateDistrict());
        }
        if (!TextUtils.isEmpty(address.getState())) {
            builder.append(",")
                    .append(address.getState());
        }
        if (!TextUtils.isEmpty(address.getCountry())) {
            builder.append(",")
                    .append(address.getCountry());
        }
        return builder.toString();
    }

    @Nullable
    public GeoPoint location() {
        try {
            return new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lon));
        } catch (Exception e) {
            return null;
        }
    }

}