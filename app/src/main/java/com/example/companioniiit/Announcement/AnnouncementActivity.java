package com.example.companioniiit.Announcement;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.companioniiit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnnouncementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnnouncementAdapter announcementAdapter;
    private List<Announcement> announcementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        recyclerView = findViewById(R.id.recycler_view_announcements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        announcementList = new ArrayList<>();
        announcementAdapter = new AnnouncementAdapter(announcementList);
        recyclerView.setAdapter(announcementAdapter);

        fetchAllAnnouncements();
    }

    private void fetchAllAnnouncements() {
        DatabaseReference hostsReference = FirebaseDatabase.getInstance().getReference().child("hosts");
        hostsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                announcementList.clear();
                for (DataSnapshot hostSnapshot : dataSnapshot.getChildren()) {
                    if (hostSnapshot.hasChild("announcements")) {
                        DataSnapshot announcementsSnapshot = hostSnapshot.child("announcements");
                        for (DataSnapshot announcementSnapshot : announcementsSnapshot.getChildren()) {
                            Announcement announcement = announcementSnapshot.getValue(Announcement.class);
                            announcementList.add(announcement);
                        }
                    }
                }
                // Sort announcements by timestamp in descending order
                Collections.sort(announcementList, new Comparator<Announcement>() {
                    @Override
                    public int compare(Announcement a1, Announcement a2) {
                        return a2.getTimestamp().compareTo(a1.getTimestamp());
                    }
                });
                announcementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AnnouncementActivity.this, "Failed to load announcements.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}