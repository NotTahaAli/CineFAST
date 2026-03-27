package com.l230954.cinefast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    TabLayout tabDateSelect;
    ViewPager2 vpMoviesList;
    ViewPagerAdapterForHomeFragment adapter;
    TabLayoutMediator mediator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(@NonNull View parentView) {
        tabDateSelect = parentView.findViewById(R.id.tabDateSelect);
        vpMoviesList = parentView.findViewById(R.id.vpMoviesList);
        adapter = new ViewPagerAdapterForHomeFragment(this.requireActivity());
        vpMoviesList.setAdapter(adapter);
        mediator = new TabLayoutMediator(tabDateSelect, vpMoviesList, (tab, position) -> {
            switch (position)
            {
                case 0:
                    tab.setText(R.string.today);
                    break;
                case 1:
                    tab.setText(R.string.tomorrow);
                    break;
            }
        });

        mediator.attach();
    }
}