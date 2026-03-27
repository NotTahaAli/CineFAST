package com.l230954.cinefast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapterForHomeFragment extends FragmentStateAdapter {
    public ViewPagerAdapterForHomeFragment(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new ComingSoonFragment();
        }
        return new NowShowingFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
