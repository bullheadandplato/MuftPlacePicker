package com.bullhead.android.muftplacepicker.domain;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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
public class Address implements Serializable {
    @SerializedName("road")
    private String road;
    @SerializedName("village")
    private String village;
    @SerializedName("state_district")
    private String stateDistrict;
    @SerializedName("state")
    private String state;
    @SerializedName("postcode")
    private String postcode;
    @SerializedName("country")
    private String country;
    @SerializedName("country_code")
    private String countryCode;

}