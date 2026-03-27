package com.l230954.cinefast;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements FragmentController {

    FragmentManager fragManager;
    HomeFragment fragHome;
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
}