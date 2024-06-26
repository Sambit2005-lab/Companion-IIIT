package com.example.companioniiit.SocietyFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewPagerAdapternaps extends FragmentStateAdapter {

    public viewPagerAdapternaps(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new about_naps();
            case 1:
                return new chat_naps();
            default:
                return new about_naps();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

