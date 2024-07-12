package com.codexnovas.companioniiit.SocietyFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewPagerAdapterMegaHertz extends FragmentStateAdapter {

    public viewPagerAdapterMegaHertz(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new about_megahertz();
            case 1:
                return new chat_megahertz();
            default:
                return new about_megahertz();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}


