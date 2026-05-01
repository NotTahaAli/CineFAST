package com.l230954.cinefast;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
    EditText etSearch;
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

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String search = etSearch.getText().toString().trim();
                Query query = reference;
                if (!search.isEmpty()) {
                    query = query.orderByChild("movie").startAt(search).endAt(search + "\uf8ff");
                }
                FirebaseRecyclerOptions<Bookings> options = new FirebaseRecyclerOptions.Builder<Bookings>()
                        .setQuery(query, Bookings.class).build();
                adapter.updateOptions(options);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    private void init(View view) {
        context = requireContext();
        controller = (NavigationController) requireActivity();
        btnMenu = view.findViewById(R.id.btnMenu);
        rvBookings = view.findViewById(R.id.rvBookings);
        etSearch = view.findViewById(R.id.etSearch);

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