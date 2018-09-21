package com.fibelatti.raffler.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {
    private KeyboardUtils() {
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));

        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
        view.requestFocus();
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
