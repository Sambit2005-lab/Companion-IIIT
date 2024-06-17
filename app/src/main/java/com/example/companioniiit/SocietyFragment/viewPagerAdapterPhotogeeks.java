package com.example.companioniiit.SocietyFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewPagerAdapterPhotogeeks extends FragmentStateAdapter {
    public viewPagerAdapterPhotogeeks(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new about_photogeeks();
            case 1:
                return new chat_photogeeks();
            default:
                return new about_photogeeks();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
