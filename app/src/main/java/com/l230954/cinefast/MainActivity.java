package com.l230954.cinefast;

import android.content.Intent;
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
    }

    @Override
    public void showBooking(Movies movie, String date) {
        Bundle args = new Bundle();
        args.putInt("id_key", MoviesDirectory.getMovieIndex(movie));
        args.putString("date_key", date);
        fragSeatSelection.setArguments(args);
        fragManager.beginTransaction()
                .hide(fragHome)
                .show(fragSeatSelection)
                .hide(fragSnacks)
                .addToBackStack("home")
                .commit();
    }

    @Override
    public void showHome() {
        if (fragManager.getBackStackEntryCount() > 0) {
            fragManager.popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragManager.beginTransaction()
                .show(fragHome)
                .hide(fragSeatSelection)
                .hide(fragSnacks)
                .commit();
    }

    @Override
    public void showBookingConfirmation(Movies movie, String date, ArrayList<String> seats, ArrayList<Snack> snacks) {
        showHome();
        Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, BookingConfirmationActivity.class);
        i.putExtra("id_key", MoviesDirectory.getMovieIndex(movie))
                .putExtra("date_key", date)
                .putExtra("seats_key", seats)
                .putExtra("snacks_key", snacks);
        startActivity(i);
    }

    Movies movie;
    String date;
    ArrayList<String> seats;
    @Override
    public void showSnacks(Movies movie, String date, ArrayList<String> seats) {
        this.movie = movie;
        this.date = date;
        this.seats = seats;
        Bundle arg = new Bundle();
        arg.putInt("id_key", MoviesDirectory.getMovieIndex(movie));
        arg.putString("date_key", date);
        arg.putStringArrayList("seats_key", seats);
        fragSnacks.setArguments(arg);
        fragManager.beginTransaction()
                .hide(fragHome)
                .hide(fragSeatSelection)
                .show(fragSnacks)
                .addToBackStack(null)
                .commit();
    }
}