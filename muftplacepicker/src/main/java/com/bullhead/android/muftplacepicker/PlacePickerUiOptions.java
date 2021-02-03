package com.bullhead.android.muftplacepicker;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Options for CloseableActivity
 * Use this class to define colors and title of activity
 */
public class PlacePickerUiOptions implements Serializable {
    private final int primaryColor;
    private final int secondaryColor;
    private final int searchTextColor;

    private PlacePickerUiOptions(int primaryColor, int secondaryColor, int title) {
        this.primaryColor    = primaryColor;
        this.secondaryColor  = secondaryColor;
        this.searchTextColor = title;
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public int getSearchTextColor() {
        return searchTextColor;
    }

    public static class Builder {
        private int primaryColor   = 0;
        private int secondaryColor = 0;

        private int title;

        private Builder() {
        }

        public Builder searchTextColor(@ColorRes int color) {
            this.title = color;
            return this;
        }

        public Builder primaryColor(@ColorRes int color) {
            this.primaryColor = color;
            return this;
        }

        public Builder secondaryColor(@ColorRes int color) {
            this.secondaryColor = color;
            return this;
        }

        public PlacePickerUiOptions build() {
            if (primaryColor == 0 || secondaryColor == 0 || title == 0) {
                throw new IllegalArgumentException("primary , search text color " +
                        "and secondary colors are compulsory");
            }
            return new PlacePickerUiOptions(primaryColor, secondaryColor, title);
        }
    }
}