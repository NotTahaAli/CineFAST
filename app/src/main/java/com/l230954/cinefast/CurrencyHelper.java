package com.l230954.cinefast;

import android.icu.text.NumberFormat;

public class CurrencyHelper {
    private static final NumberFormat format = NumberFormat.getCurrencyInstance();
    public static String formatCurrency(float amount) {
        return format.format(amount);
    }
}
