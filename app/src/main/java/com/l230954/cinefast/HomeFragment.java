package com.l230954.cinefast;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    TabLayout tabDateSelect;
    ViewPager2 vpMoviesList;
    ViewPagerAdapterForHomeFragment adapter;
    TabLayoutMediator mediator;
    Context context;
    ImageView ivMenu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        hookButtons();
    }

    private void init(@NonNull View parentView) {
        tabDateSelect = parentView.findViewById(R.id.tabDateSelect);
        vpMoviesList = parentView.findViewById(R.id.vpMoviesList);
        ivMenu = parentView.findViewById(R.id.ivMenu);
        adapter = new ViewPagerAdapterForHomeFragment(this.requireActivity());
        context = this.requireContext();
        vpMoviesList.setAdapter(adapter);
        mediator = new TabLayoutMediator(tabDateSelect, vpMoviesList, (tab, position) -> {
            View customView = LayoutInflater.from(context).inflate(R.layout.date_selector_tab, null);
            TextView tabText = customView.findViewById(R.id.tabText);
            switch (position)
            {
                case 0:
                    tabText.setText(R.string.today);
                    break;
                case 1:
                    tabText.setText(R.string.tomorrow);
                    break;
            }
            tab.setCustomView(customView);
        });

        mediator.attach();
    }

    private void hookButtons() {
        ivMenu.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });
    }
}
