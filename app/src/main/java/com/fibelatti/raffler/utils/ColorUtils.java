package com.fibelatti.raffler.utils;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class ColorUtils {
    /***
     * This code is not used anymore but it is useful, so it will be kept here.
     *
     * @param startColor
     * @param endColor
     * @param steps
     * @return
     */
    public static List<Integer> calculateColorGradient(int startColor, int endColor, int steps) {
        List<Integer> colors = new ArrayList<>();

        int r1 = Color.red(startColor);
        int g1 = Color.green(startColor);
        int b1 = Color.blue(startColor);

        int r2 = Color.red(endColor);
        int g2 = Color.green(endColor);
        int b2 = Color.blue(endColor);

        int redStep = (r2 - r1) / steps;
        int greenStep = (g2 - g1) / steps;
        int blueStep = (b2 - b1) / steps;

        for (int i = 0; i <= steps; i++)
            colors.add(Color.rgb(r1 + redStep * i, g1 + greenStep * i, b1 + blueStep * i));

        return colors;
    }
}
