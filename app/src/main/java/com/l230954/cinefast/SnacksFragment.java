package com.l230954.cinefast;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class SnacksFragment extends Fragment {
    MaterialButton btnConfirm;
    ArrayList<Snack> snacks;
    ListView lvSnacks;
    SnacksAdapter adapter;
    Context context;
    NavigationController controller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_snacks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        hookButtons();
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        reset();
    }

    private void hookButtons() {
        btnConfirm.setOnClickListener(v->{
            Bundle args = getArguments();
            if (args == null) {
                controller.goBackToHome();
            } else {
                Movies movie = MoviesDirectory.getMovie(args.getInt("id_key", 0));
                String date = args.getString("date_key");
                ArrayList<String> seats = args.getStringArrayList("seats_key");
                controller.showBookingConfirmation(movie, date, seats, snacks);
            }
        });
    }
    private void init(View parent) {
        controller = (NavigationController) this.getActivity();
        context = requireContext();
        btnConfirm = parent.findViewById(R.id.btnConfirm);
        lvSnacks = parent.findViewById(R.id.lvSnacks);
        reset();
    }

    private void reset() {
        DBManager manager = new DBManager(context);
        manager.open();
        snacks = manager.getSnacks();
        manager.close();
        adapter = new SnacksAdapter(context, snacks);
        lvSnacks.setAdapter(adapter);
    }
}