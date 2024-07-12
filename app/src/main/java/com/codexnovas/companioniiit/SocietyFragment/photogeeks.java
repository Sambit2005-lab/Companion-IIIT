package com.codexnovas.companioniiit.SocietyFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.codexnovas.companioniiit.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class photogeeks extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photogeeks);

         TabLayout tabLayout = findViewById(R.id.tab_layoutphoto);
        ViewPager2 viewPager = findViewById(R.id.view_pagerphoto);

        viewPager.setAdapter(new viewPagerAdapterPhotogeeks(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("About");
                    break;
                case 1:
                    tab.setText("Chat");
                    break;
            }
        }).attach();
    }
}