package com.l230954.cinefast;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MyBookingsFragment extends Fragment {
    ImageView btnMenu;
    RecyclerView rvBookings;
    NavigationController controller;
    Context context;
    BookingsAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_bookings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);

        btnMenu.setOnClickListener(v -> {
            controller.showMenu();
        });
    }

    private void init(View view) {
        context = requireContext();
        controller = (NavigationController) requireActivity();
        btnMenu = view.findViewById(R.id.btnMenu);
        rvBookings = view.findViewById(R.id.rvBookings);

        database = FirebaseDatabase.getInstance(getString(R.string.firebase_database_url));
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        assert user != null;
        reference = database.getReference("bookings").child(user.getUid());

        Query query = reference;
        FirebaseRecyclerOptions<Bookings> options = new FirebaseRecyclerOptions.Builder<Bookings>()
                .setQuery(query, Bookings.class).build();
        adapter = new BookingsAdapter(options, requireContext());
        rvBookings.setHasFixedSize(true);
        rvBookings.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvBookings.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}