package com.codexnovas.companioniiit.SocietyFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewPagerAdapterTars extends FragmentStateAdapter {
    public viewPagerAdapterTars(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new about_tars();
            case 1:
                return new chat_tars();
            default:
                return new about_tars();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
