package com.l230954.cinefast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ComingSoonFragment extends Fragment {
    RecyclerView rvMovies;
    MoviesAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coming_soon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(@NonNull View parentView) {
        rvMovies = parentView.findViewById(R.id.rvMovies);
        adapter = new MoviesAdapter(MoviesDirectory.getTomorrowMovies(), CalenderHelper.GetDate(1), (FragmentController) this.requireActivity());
        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        rvMovies.setAdapter(adapter);
    }
}