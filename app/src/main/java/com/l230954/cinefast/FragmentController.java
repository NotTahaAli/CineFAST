package com.l230954.cinefast;

import java.util.ArrayList;

public interface FragmentController {
    void showBooking(Movies movie, String date);
    void showHome();
    void showBookingConfirmation(Movies movie, String date, ArrayList<String> seats, ArrayList<Snack> snacks);
    void showSnacks(Movies movie, String date, ArrayList<String> seats);
}
