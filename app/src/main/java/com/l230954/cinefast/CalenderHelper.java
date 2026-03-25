package com.l230954.cinefast;

import android.icu.util.Calendar;

public class CalenderHelper {
    private static String padStart(String s, Character c, int len) {
        if (s.length() >= len) return s;
        int difference = len - s.length();
        return c.toString().repeat(difference) + s;
    }
    public static String GetDate(int DaysLater) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, DaysLater);
        String day = padStart(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)), '0', 2);
        String month = padStart(Integer.toString(calendar.get(Calendar.MONTH) + 1), '0', 2);
        String year = padStart(Integer.toString(calendar.get(Calendar.YEAR)), '0', 4);
        return day + "/" + month + "/" + year;
    }
}
