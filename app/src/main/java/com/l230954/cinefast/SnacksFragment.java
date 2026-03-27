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
    FragmentController controller;

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

    private void hookButtons() {
        btnConfirm.setOnClickListener(v->{
            Bundle args = getArguments();
            if (args == null) {
                controller.showHome();
            } else {
                Movies movie = MoviesDirectory.getMovie(args.getInt("id_key", 0));
                String date = args.getString("date_key");
                ArrayList<String> seats = args.getStringArrayList("seats_key");
                controller.showBookingConfirmation(movie, date, seats, snacks);
            }
        });
    }
    private void init(View parent) {
        controller = (FragmentController) this.getActivity();
        context = requireContext();
        btnConfirm = parent.findViewById(R.id.btnConfirm);
        lvSnacks = parent.findViewById(R.id.lvSnacks);
        snacks = new ArrayList<>();
        snacks.add(new Snack(getResources().getString(R.string.snacks1), 5.0f, getResources().getString(R.string.snacks1_description), R.drawable.snacks1));
        snacks.add(new Snack(getResources().getString(R.string.snacks2), 10.0f, getResources().getString(R.string.snacks2_description), R.drawable.snacks2));
        snacks.add(new Snack(getResources().getString(R.string.snacks3), 15.0f, getResources().getString(R.string.snacks3_description), R.drawable.snacks3));
        snacks.add(new Snack(getResources().getString(R.string.snacks4), 2.5f, getResources().getString(R.string.snacks4_description), R.drawable.snacks4));
        adapter = new SnacksAdapter(context, snacks);
        lvSnacks.setAdapter(adapter);
    }
}