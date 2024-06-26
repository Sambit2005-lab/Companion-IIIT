package com.example.companioniiit.SocietyFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewPagerAdapterVedantasamiti extends FragmentStateAdapter {

    public viewPagerAdapterVedantasamiti(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new about_vedantasamiti();
            case 1:
                return new chat_vedantasamiti();
            default:
                return new about_vedantasamiti();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
