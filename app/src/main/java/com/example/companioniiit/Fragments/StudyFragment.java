package com.example.companioniiit.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.companioniiit.R;
import com.example.companioniiit.Adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class StudyFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;

    public StudyFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study, container, false);

        // Initialize TabLayout and ViewPager
        tabLayout = view.findViewById(R.id.tab);
        viewPager = view.findViewById(R.id.viewpager);

        // Set up ViewPager with the adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        // Link TabLayout with ViewPager
        tabLayout.setupWithViewPager(viewPager);




        return view;
    }
}
