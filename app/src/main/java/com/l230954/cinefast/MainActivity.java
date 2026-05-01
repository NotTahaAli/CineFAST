package com.l230954.cinefast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationController {

    FragmentManager fragManager;
    HomeFragment fragHome;
    SnacksFragment fragSnacks;
    SeatSelectionFragment fragSeatSelection;
    BookingConfirmationFragment fragBookingConfirmation;
    MyBookingsFragment fragMyBookings;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        setupDrawer();
        fetchUserInfo();
        goBackToHome();
        fragManager.beginTransaction()
            .hide(fragMyBookings)
            .commit();
    }

    private void init() {
        fragManager = getSupportFragmentManager();
        fragHome = (HomeFragment) fragManager.findFragmentById(R.id.fragHome);
        fragSeatSelection = (SeatSelectionFragment) fragManager.findFragmentById(R.id.fragSeatSelection);
        fragSnacks = (SnacksFragment) fragManager.findFragmentById(R.id.fragSnacks);
        fragBookingConfirmation = (BookingConfirmationFragment) fragManager.findFragmentById(R.id.fragBookingConfirmation);
        fragMyBookings = (MyBookingsFragment) fragManager.findFragmentById(R.id.fragMyBookings);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance(getString(R.string.firebase_database_url)).getReference();
    }

    private void setupDrawer() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                showHome();
            } else if (id == R.id.nav_bookings) {
                showMyBookings();
            } else if (id == R.id.nav_logout) {
                logout();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public void showMenu() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void fetchUserInfo() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            View headerView = navigationView.getHeaderView(0);
            TextView tvUserName = headerView.findViewById(R.id.tvUserName);
            TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);

            tvUserEmail.setText(user.getEmail());

            databaseReference.child("users").child(user.getUid()).child("name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                tvUserName.setText(snapshot.getValue(String.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
        }
    }

    private void logout() {
        auth.signOut();
        // Clear remember me
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("rememberMe", false).apply();
        
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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
        if (fragMyBookings.isHidden()) return;
        fragManager.beginTransaction()
                .hide(fragMyBookings)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goBackToHome() {
        if (fragManager.getBackStackEntryCount() > 0) {
            fragManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
        assert auth.getCurrentUser() != null;
        Bookings booking = new Bookings(movie.name, date, movie.time, seats.size());
        databaseReference.child("bookings").child(auth.getCurrentUser().getUid()).push().setValue(booking.toMap())
            .addOnSuccessListener(v->{
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
            })
            .addOnFailureListener(e->{
                Toast.makeText(this, "Failed to make Booking, " + e.getMessage() + "!", Toast.LENGTH_SHORT).show();
            });
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

    @Override
    public void showMyBookings() {
        if (!fragMyBookings.isHidden()) return;
        fragManager.beginTransaction()
                .show(fragMyBookings)
                .addToBackStack(null)
                .commit();
    }
}
