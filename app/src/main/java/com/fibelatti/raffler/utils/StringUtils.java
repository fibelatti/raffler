package com.fibelatti.raffler.utils;

/**
 * Created by fibelatti on 02/08/16.
 */
public class StringUtils {
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }
}
