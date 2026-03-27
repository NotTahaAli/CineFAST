package com.l230954.cinefast;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    TabLayout tabDateSelect;
    ViewPager2 vpMoviesList;
    ViewPagerAdapterForHomeFragment adapter;
    TabLayoutMediator mediator;
    Context context;
    ImageView ivMenu;
    MaterialButton btnLastBooking;

    SharedPreferences sPref;

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
        btnLastBooking = parentView.findViewById(R.id.btnLastBooking);
        adapter = new ViewPagerAdapterForHomeFragment(this.requireActivity());
        context = this.requireContext();
        sPref = context.getSharedPreferences("last_booking", MODE_PRIVATE);
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
        ivMenu.setOnClickListener(v->{
            if (btnLastBooking.getVisibility() == View.GONE) {
                btnLastBooking.setVisibility(View.VISIBLE);
            } else {
                btnLastBooking.setVisibility(View.GONE);
            }
        });
        btnLastBooking.setOnClickListener(v->{
        if (!sPref.contains("name")) {
            new AlertDialog.Builder(context).setTitle("No Last Booking").create().show();
        } else {
            String name = sPref.getString("name", "");
            int seats = sPref.getInt("seats", 0);
            float totalPrice = sPref.getFloat("total", 0);

            new AlertDialog.Builder(context)
                    .setTitle("Last Booking")
                    .setMessage("Movie: " + name + "\nSeats: " + seats + "\nTotal Price: " + CurrencyHelper.formatCurrency(totalPrice))
                    .create().show();
        }
        });
    }
}