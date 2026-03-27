package com.l230954.cinefast;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentController {

    FragmentManager fragManager;
    HomeFragment fragHome;
    SeatSelectionFragment fragSeatSelection;

    ActivityResultLauncher<Intent> snacksLauncher;

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

        snacksLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleSnacksResult
        );
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
                .commit();
    }

    @Override
    public void showBookingConfirmation(Movies movie, String date, ArrayList<String> seats, ArrayList<Snack> snacks) {
        showHome();
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
        Intent i = new Intent(this, SnacksActivity.class);
        i.putExtra("id_key", MoviesDirectory.getMovieIndex(movie))
                .putExtra("date_key", date);
        startActivity(i);
    }

    public void handleSnacksResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_CANCELED) return;
        showHome();
        Intent i = result.getData();
        if (i == null) return;
        ArrayList<Snack> snacks = (ArrayList<Snack>) i.getSerializableExtra("snacks_key");
        showBookingConfirmation(movie, date, seats, snacks);
    }
}