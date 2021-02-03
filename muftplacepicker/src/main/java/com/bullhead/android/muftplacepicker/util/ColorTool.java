package com.bullhead.android.muftplacepicker.util;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.bullhead.android.muftplacepicker.R;
import com.google.android.material.color.MaterialColors;

public final class ColorTool {
    private ColorTool() {
    }

    public static int primaryColor(@NonNull Context context) {
        int color = MaterialColors.getColor(context, R.attr.colorAccent, 0);
        if (color == 0) {
            //new theme have colorPrimary
            color = MaterialColors.getColor(context, R.attr.colorPrimary,
                    Color.parseColor("#4caf50"));
        }
        return color;
    }

    public static int secondaryColor(@NonNull Context context) {
        return MaterialColors.getColor(context, android.R.attr.windowBackground, Color.WHITE);
    }
}
