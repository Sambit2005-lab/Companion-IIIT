package com.example.companioniiit.SocietyFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.companioniiit.chat_techsociety;

public class viewPagerAdapterTechsociety extends FragmentStateAdapter {

    public viewPagerAdapterTechsociety(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new about_techsociety();
            case 1:
                return new chat_techsociety();
            default:
                return new about_techsociety();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
