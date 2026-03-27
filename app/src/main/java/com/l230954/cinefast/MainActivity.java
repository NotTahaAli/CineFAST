package com.l230954.cinefast;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentController {

    FragmentManager fragManager;
    HomeFragment fragHome;
    SnacksFragment fragSnacks;
    SeatSelectionFragment fragSeatSelection;
    BookingConfirmationFragment fragBookingConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        showHome();
    }

    private void init() {
        fragManager = getSupportFragmentManager();
        fragHome = (HomeFragment) fragManager.findFragmentById(R.id.fragHome);
        fragSeatSelection = (SeatSelectionFragment) fragManager.findFragmentById(R.id.fragSeatSelection);
        fragSnacks = (SnacksFragment) fragManager.findFragmentById(R.id.fragSnacks);
        fragBookingConfirmation = (BookingConfirmationFragment) fragManager.findFragmentById(R.id.fragBookingConfirmation);
    }

    @Override
    public void showBooking(Movies movie, String date) {
        Bundle args = new Bundle();
        args.putInt("id_key", MoviesDirectory.getMovieIndex(movie));
        args.putString("date_key", date);
        fragSeatSelection.setArguments(args);
        fragManager.beginTransaction()
                .hide(fragHome)
                .hide(fragBookingConfirmation)
                .hide(fragSnacks)
                .show(fragSeatSelection)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void showHome() {
        if (fragManager.getBackStackEntryCount() > 0) {
            fragManager.popBackStack();
        }
        fragManager.beginTransaction()
                .hide(fragSeatSelection)
                .hide(fragSnacks)
                .hide(fragBookingConfirmation)
                .show(fragHome)
                .commit();
    }

    @Override
    public void showBookingConfirmation(Movies movie, String date, ArrayList<String> seats, ArrayList<Snack> snacks) {
        if (snacks != null && fragManager.getBackStackEntryCount() > 0) {
            fragManager.popBackStack();
        }
        Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
        Bundle args = new Bundle();
        args.putInt("id_key", MoviesDirectory.getMovieIndex(movie));
        args.putString("date_key", date);
        args.putStringArrayList("seats_key", seats);
        args.putSerializable("snacks_key", snacks);
        fragBookingConfirmation.setArguments(args);
        fragManager.beginTransaction()
                .hide(fragSnacks)
                .hide(fragSeatSelection)
                .hide(fragHome)
                .show(fragBookingConfirmation)
                .commit();
    }

    @Override
    public void showSnacks(Movies movie, String date, ArrayList<String> seats) {
        Bundle arg = new Bundle();
        arg.putInt("id_key", MoviesDirectory.getMovieIndex(movie));
        arg.putString("date_key", date);
        arg.putStringArrayList("seats_key", seats);
        fragSnacks.setArguments(arg);
        fragManager.beginTransaction()
                .hide(fragSeatSelection)
                .show(fragSnacks)
                .addToBackStack(null)
                .commit();
    }
}