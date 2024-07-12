package com.codexnovas.companioniiit.SocietyFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewPagerAdapterparacosm extends FragmentStateAdapter {

    public viewPagerAdapterparacosm(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new about_paracosm();
            case 1:
                return new chat_paracosm();
            default:
                return new about_paracosm();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}



