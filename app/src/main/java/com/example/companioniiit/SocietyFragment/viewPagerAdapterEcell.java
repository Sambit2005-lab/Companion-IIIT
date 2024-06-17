package com.example.companioniiit.SocietyFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewPagerAdapterEcell extends FragmentStateAdapter {

    public viewPagerAdapterEcell(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new about_ecell();
            case 1:
                return new chat_ecell();
            default:
                return new about_ecell();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

