package com.example.companioniiit.MyCalendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companioniiit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class holidayList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<eventHoliday> eventList;
    private List<eventHoliday> filteredList;
    private DatabaseReference databaseReference;
    private ImageButton backbutton;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holidaylist);

        recyclerView = findViewById(R.id.recycler_view_Holiday_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        filteredList = new ArrayList<>();
        eventAdapter = new EventAdapter(filteredList);
        recyclerView.setAdapter(eventAdapter);

        backbutton = findViewById(R.id.back_button);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holidayList.this, activity_myCalender_card.class);
                startActivity(intent);
            }
        });

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterEvents(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterEvents(newText);
                return false;
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("hosts");
        fetchEvents();
    }

    private void fetchEvents() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot hostSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot eventsSnapshot = hostSnapshot.child("events");
                    for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                        eventHoliday event = eventSnapshot.getValue(eventHoliday.class);
                        if (event != null) {
                            Log.d("Event", "Date: " + event.getDate() + ", Description: " + event.getDescription());
                            eventList.add(event);
                        }
                    }
                }
                filterEvents(searchView.getQuery().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", databaseError.getMessage());
            }
        });
    }

    private void filterEvents(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(eventList);
        } else {
            for (eventHoliday event : eventList) {
                if (event.getDate().contains(query)) {
                    filteredList.add(event);
                }
            }
        }
        eventAdapter.notifyDataSetChanged();
    }
}